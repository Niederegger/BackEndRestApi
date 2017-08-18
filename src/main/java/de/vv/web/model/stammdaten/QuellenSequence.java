package de.vv.web.model.stammdaten;

import java.util.ArrayList;
import java.util.List;

import de.vv.web.model.Strings2;

public class QuellenSequence {
	public List<QuellenAngabe> quellen = new ArrayList<QuellenAngabe>();
	public List<String> values = new ArrayList<String>();
	public String level2;
	public int sequNum;

	public QuellenSequence(String l2, int sn) {
		level2 = l2;
		sequNum = sn;
	}

	/**
	 * 
	 * @param quelle
	 *          str1 -> origin,
	 *          str2 -> source
	 * @param val
	 *          str1 -> value,
	 *          str2 -> timestamp
	 */
	public void insert(Strings2 quelle, Strings2 val) {
		if (!values.contains(val.str1)) {	// wenn der Wert bereits vorhanden ist, wird er nicht benoetigt
			boolean flag = false;
			QuellenAngabe dummy = new QuellenAngabe(quelle);
			for (QuellenAngabe qa : quellen) {
				if (qa.equals(dummy)) { // diese Quelle exestiert bereits, fuege den Wert der Quelle bei
					flag = true;
					qa.insert(val);
					break;
				}
			}
			if (!flag) { 							// diese Quelle ist nocht nicht vorhanden, initialisiere diese
				dummy.insert(val);
				quellen.add(dummy);
			}
			values.add(val.str1);
		}
	}

	public void done() {
		values = null;
	}

	@Override
	public int hashCode() {
		return level2.hashCode() * 5;
	}

	@Override
	public boolean equals(Object obj) {
		assert (obj instanceof QuellenSequence);
		QuellenSequence o = (QuellenSequence) obj;
		return o.level2.equals(level2);
	}

}
