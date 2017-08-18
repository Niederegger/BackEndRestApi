package de.vv.web.model.stammdaten;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import de.vv.web.db.DBC_WP;
import de.vv.web.model.Strings2;
import de.vv.web.model.maininfo.MainInfo2;

public class QuellenSet {
//	public List<QuellenSequence> all = new ArrayList<QuellenSequence>();
	public List<Integer> seqNums = new ArrayList<Integer>();
	public HashMap<String, List<QuellenSequence>> container = new HashMap<String, List<QuellenSequence>>();
	public List<String> keyOrder = new ArrayList<String>();
	
	public QuellenSet(List<MainInfo2> container){
		workEntries(container);
	}
	
	
	public void workEntries(List<MainInfo2> container){
		int currSeq = -1;
		String ck = "";
		QuellenSequence qs = null;
		for(MainInfo2 mi2 : container){
			mi2.normalize();
			// this part adds the done QuellenSequence to th list and creates a new one
			if(currSeq != mi2.sequence_num){
				if(qs != null) {
					qs.done();
					insertHM(ck, qs);
				};
				currSeq	= mi2.sequence_num;
				ck	= mi2.level1;
				seqNums.add(currSeq);
				qs = new QuellenSequence(mi2.level2, currSeq);
			} 
			// here the information of maininfo2 are parsed to the current QuellSequence
			qs.insert(new Strings2(mi2.data_origin, mi2.urlsource), new Strings2(mi2.stringValue, mi2.timestamp));
		}
		insertHM(ck, qs); //  a last insert since, the loop couldn't catch the last sequenceNum
		
		// adding all missed fields
		for(MainInfo2 fields : DBC_WP.getFields(null)){
			ck = fields.level1;
			if(!this.container.keySet().contains(ck)){
				fields.normalize();
				insertHM(ck, new QuellenSequence(fields.level2, fields.sequence_num)); //  add empty Field
			}
		}
	}
	
	void insertHM(String key, QuellenSequence item){
		if(container.keySet().contains(key)){
			List<QuellenSequence> lQS = container.get(key);
			lQS.add(item);
			container.put(key, lQS);
		} else {
			ArrayList<QuellenSequence> al = new ArrayList<QuellenSequence>();
			al.add(item);
			container.put(key, al);
			keyOrder.add(key);
		}
	}
	
}
