package de.vv.web.model.maininfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.vv.web.model.StringInt;
import de.vv.web.model.stammdaten.QuellenSet;

public class MainInfo2Container {

	//--------------------------------------------------------------------------------------------------------------------
	// Variables:
	//--------------------------------------------------------------------------------------------------------------------

	public List<MainInfo2> container = new ArrayList<MainInfo2>();
	public HashMap<Integer, StringInt[]> quelRef = new HashMap<Integer, StringInt[]>();

	//--------------------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------------------

	public MainInfo2Container() {
	}

	/**
	 * Initializes this container with fetched ResultSet (only suited ResultSets will work, use the right query m8 ;)
	 * 
	 * @param rs
	 */
	public MainInfo2Container(ResultSet rs) {
		init(rs);
	}

	//--------------------------------------------------------------------------------------------------------------------
	// init
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * Initializes this container with fetched ResultSet (only suited ResultSets will work, use the right query m8 ;)
	 * 
	 * @param rs
	 */
	public void init(ResultSet rs) {
		try {
			while (rs.next()) {
				MainInfo2 mi2 = new MainInfo2(rs);
				if (mi2.stringValue != null && !mi2.stringValue.equals("")){
					container.add(mi2);
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	//Converter to
	//--------------------------------------------------------------------------------------------------------------------

	public QuellenSet toQuellenSet() {
		return new QuellenSet(container);
	}

	public QuellenMap toQuellenContainer() {
		sortContainer(true);
		return new QuellenMap(container, quelRef);
	}

	public MainInfoContainer getStammdaten() {
		MainInfoContainer mic = new MainInfoContainer();
		List<Integer> doneSeq = new ArrayList<Integer>();
		MainInfo2 accu;
		for (MainInfo2 mi2a : container) {
			accu = mi2a;
			if (!doneSeq.contains(mi2a.sequence_num)) {
				for (MainInfo2 mi2b : container) {
					if (accu.sameSeq(mi2b))
						if (accu.higherPrior(mi2b))
							accu = mi2b;
				}
				accu.normalize();
				mic.instert(accu.level1, accu.level2, accu.stringValue);
				doneSeq.add(accu.sequence_num);
			}
		}
		return mic;
	}

	public MainInfo2[] getSameSqn(int sqn) {
		List<MainInfo2> ret = new ArrayList<MainInfo2>();
		for (MainInfo2 mi2 : container) {
			if (sqn == mi2.sequence_num) {
				mi2.normalize();
				ret.add(mi2);
			}
		}
		MainInfo2[] arr = ret.toArray(new MainInfo2[ret.size()]);
		MainInfo2 mi2;
		for (int i = 0; i < ret.size(); i++) {
			for (int j = i + 1; j < ret.size(); j++) {
				if (arr[j].sequence_num < arr[i].sequence_num) {
					mi2 = arr[i];
					arr[i] = arr[j];
					arr[j] = mi2;
				}
			}
		}
		return arr;
	}

	public QuellElement toQuellElement(int sqn, int srcn) {
		for (MainInfo2 k : container) {
			if (k.sameSqSn(sqn, srcn)) {
				k.normalize();
				return new QuellElement(k.level1, k.toQuellenEntry());
			}
		}
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// Functions
	//--------------------------------------------------------------------------------------------------------------------

	public void sortContainer(boolean quellenNeeded) {
		List<MainInfo2> newContainer = new ArrayList<MainInfo2>();
		List<Integer> doneSeq = new ArrayList<Integer>();
		MainInfo2 accu;
		List<StringInt> qref;
		for (MainInfo2 mi2a : container) {
			accu = mi2a;
			if (!doneSeq.contains(mi2a.sequence_num)) {
				qref = new ArrayList<StringInt>();
				for (MainInfo2 mi2b : container) {
					if (accu.sameSeq(mi2b)) {
						if (accu.higherPrior(mi2b))
							accu = mi2b;
						if (quellenNeeded) {					// nimmt quellenReferencen mit <- dient der DropDown kultivierung
							String str = mi2b.sourceId;
							StringInt si = new StringInt(str, mi2b.source_num);
							if (!qref.contains(si)) {
								qref.add(si);
							}
						}
					}
				}
				quelRef.put(accu.sequence_num, qref.toArray(new StringInt[qref.size()]));
				accu.normalize();
				newContainer.add(accu);
				doneSeq.add(accu.sequence_num);
			}
		}
		container = newContainer;
	}

}
