package de.vv.web.model.maininfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.vv.web.db.DBC_WP;
import de.vv.web.model.StringInt;
import de.vv.web.model.ContainerForOldThoughts.UploadContainer;

public class QuellenMap {
	public HashMap<String, QuellenContainer> data = new HashMap<String, QuellenContainer>();
	public List<String> keyOrder = new ArrayList<String>();
	
	public QuellenMap(List<MainInfo2> container){
		for(MainInfo2 mi2 : container){
			insert(mi2);
		}
	}
	
	public QuellenMap(){	}
	
	public QuellenMap(List<MainInfo2> container, HashMap<Integer, StringInt[]> quelRef){
		for(MainInfo2 mi2 : container){
			insert(mi2, quelRef.get(mi2.sequence_num));
		}
		for(MainInfo2 si : DBC_WP.getFields(null)){
			if(!keyOrder.contains(si.level1)){
				insert(si);
			} else {
				carefulInsert(si);
			}
		}
	}
	
	public void carefulInsert(MainInfo2 mi2){
		QuellenContainer qc;
		qc = data.get(mi2.level1);
		for(QuellenEntry qe :  qc.entries){
			if(qe.lvl2 == mi2.level2) return;
		}
		insert(mi2);
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

	public String getIsin(){
		return data.get("ISIN").entries.get(0).val;
	}
	
	public UploadContainer toUploadContainer() {
		String isin = getIsin();
		if(isin == null || isin.length() != 12) return null;
		UploadContainer uc = new UploadContainer();
		uc.isin = isin;
		QuellenContainer tmp;
		for(String key : data.keySet()){
			tmp = data.get(key);
			for(QuellenEntry qe : tmp.entries){
				if(qe.lvl2==null||qe.lvl2==""){
					uc.fieldName.add(key);
				} else {
					uc.fieldName.add(key + "." + qe.lvl2);
				}
				uc.values.add(qe.val);
			}
		}
		return uc;
	}
}
