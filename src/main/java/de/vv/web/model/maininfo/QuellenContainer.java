package de.vv.web.model.maininfo;

import java.util.ArrayList;
import java.util.List;

import de.vv.web.model.StringInt;


public class QuellenContainer {
//	public List<MainInfo2> container = new ArrayList<MainInfo2>();
//	
//	
//	public void add(MainInfo2 mi2){
//		container.add(mi2);
//	}

	public List<QuellenEntry> entries = new ArrayList<QuellenEntry>();
	
	
	public QuellenContainer(){}
	
	public QuellenContainer(MainInfo2 mi2, StringInt[] q){
		insert(mi2, q);
	}
	
	public QuellenContainer(MainInfo2 mi2){
		insert(mi2);
	}
	
	public void insert(MainInfo2 mi2){
		entries.add(mi2.toQuellenEntry(null));
	}
	
	public void insert(MainInfo2 mi2, StringInt[] q){
		entries.add(mi2.toQuellenEntry(q));
	}
	
}
