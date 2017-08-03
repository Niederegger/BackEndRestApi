package de.vv.web.model.ContainerForOldThoughts;

import java.util.ArrayList;
import java.util.HashMap;

public class IsinData {

	public static final String query = "select distinct(MV_FIELDNAME), MV_STRINGVALUE, MV_AS_OF_DATE, MV_TIMESTAMP, "
			+ "MV_SOURCE_ID from vv_mastervalues where mv_isin=? order by MV_AS_OF_DATE desc, MV_TIMESTAMP desc;";
	

	// this map is used to handle the multiple fieldnames
	// some may contain multiple entrys, like closing price
	// other are contained simply once, considering the sorting after
	// a query, to use this data type more freely7
	// this simply stores each index of fieldname
	public HashMap<String, Integer> nameMap = new HashMap<String, Integer>();
	// its a list of list of idrows (isin data rows),
	// as mentioned above, some rows may appear mutliple times,
	// thats the reason for a list in a list
	public ArrayList<ArrayList<IdRow>> container = new ArrayList<ArrayList<IdRow>>();

	public void add(IdRow row){
		if(nameMap.containsKey(row.mv_fieldname)){ // append to list
			container.get(nameMap.get(row.mv_fieldname)).add(row); 
		} else { // create an entr: init container entry + nameMap
			// since size-1 is always the index of last element in list, size can be used to link the new entry
			nameMap.put(row.mv_fieldname, container.size()); 
			ArrayList<IdRow> tmp = new ArrayList<IdRow>();
			tmp.add(row);
			container.add(tmp);
		}
	}
	
	
	public void sort(){
		for(int i = 0; i < container.size(); i++){
			for(int j = i+1; j < container.size(); j++){
				if(container.get(i).size() > container.get(j).size()){
					ArrayList<IdRow> tmp = container.get(i);
					container.set(i, container.get(j));
					container.set(j, tmp);
				}
			}	
		}
		nameMapping();
	}

	public void nameMapping(){
		nameMap = new HashMap<String, Integer>();
		for(int i = 0; i < container.size(); i++){
			nameMap.put(container.get(i).get(0).mv_fieldname, i);
		}
	}
}

/*
 * its also possible to simply check each first element of container, and
 * lookup, whether the fieldnames are equal, this is neither simplier to
 * implement nor faster, having n datasources, with all different kinds of
 * fieldnames, the nameMap option is way more scalable
 */