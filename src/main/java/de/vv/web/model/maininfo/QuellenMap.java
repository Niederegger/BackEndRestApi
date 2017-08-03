package de.vv.web.model.maininfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.vv.web.model.StringInt;

public class QuellenMap {
	public HashMap<String, QuellenContainer> data = new HashMap<String, QuellenContainer>();
	public List<String> keyOrder = new ArrayList<String>();
	
	public QuellenMap(List<MainInfo2> container){
		for(MainInfo2 mi2 : container){
			insert(mi2);
		}
	}
	
	public QuellenMap(List<MainInfo2> container, HashMap<Integer, StringInt[]> quelRef){
		for(MainInfo2 mi2 : container){
			insert(mi2, quelRef.get(mi2.sequence_num));
		}
	}
	
	public void insert(MainInfo2 mi2, StringInt[] refs){
		QuellenContainer qc;
		if(keyOrder.contains(mi2.level1)){
			qc = data.get(mi2.level1);
			qc.insert(mi2, refs);
			data.put(mi2.level1, qc);
		} else {
			keyOrder.add(mi2.level1);
			data.put(mi2.level1, new QuellenContainer(mi2, refs));
		}
	}
	
	public void insert(MainInfo2 mi2){
		QuellenContainer qc;
		if(keyOrder.contains(mi2.level1)){
			qc = data.get(mi2.level1);
			qc.insert(mi2);
			data.put(mi2.level1, qc);
		} else {
			keyOrder.add(mi2.level1);
			data.put(mi2.level1, new QuellenContainer(mi2));
		}
	}
}
