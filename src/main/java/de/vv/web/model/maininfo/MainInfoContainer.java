package de.vv.web.model.maininfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.vv.web.model.ContainerForOldThoughts.UploadContainer;

//serves as a container for returned values from vvsp_get_maininfo
public class MainInfoContainer {
	// stores data with key - value pairs
	// value is designed as list, cause some keys have multiple values
	public HashMap<String, MainInfo> data = new HashMap<String, MainInfo>();
	// stores level 1 headers
	// array list is necessary since it's keeping the right order
	public List<String> keyOrder = new ArrayList<String>();

	public void instert(String key, String lvl2, String val) {
		lvl2 = lvl2 != null ? lvl2.trim() : lvl2;
		val = val != null ? val.trim() : val;
		MainInfo mi;
		if (keyOrder.contains(key)) {
			mi = data.get(key);
			mi.insert(lvl2, val);
			data.put(key, mi);
		} else { // adds new entry
			keyOrder.add(key);
			mi = new MainInfo(lvl2, val);
			data.put(key, mi);
		}
	}

	public boolean checkIsin() {
		if (data.containsKey("ISIN")) {
			return data.containsKey("ISIN");
		}
		return false;
	}
	
	public String getIsin() {
		if(checkIsin()){
			return data.get("ISIN").info.get(0).val;
		}
		return "";
	}
	
	
	//  hashMap to String
	public String hm2Str(HashMap<String, MainInfo> hm){
		StringBuilder sb = new StringBuilder();
		for(String s : hm.keySet()){
			sb.append(s);
			sb.append(":\n");
			for(D2Arr mi : hm.get(s).info){
				sb.append("\t");
				sb.append(mi);
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public UploadContainer toUploadContainer(){
		String isin = getIsin();
		if(isin.length()!=12)return null;
		UploadContainer uc = new UploadContainer();
		uc.isin = isin;
		MainInfo mi;
		for(String s : data.keySet()){
			mi = data.get(s);
			for(D2Arr info : mi.info){
				if(info.lvl2==null||info.lvl2==""){
					uc.fieldName.add(s);
				} else {
					uc.fieldName.add(info.lvl2);
				}
				uc.values.add(info.val);
			}
		}
		return uc;
	}
}
