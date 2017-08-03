package de.vv.web.model.maininfo;

import java.util.ArrayList;
import java.util.List;



// serves as a container for returned a container for a single Level1 Information
// level2 and stringValue are bound by index values
public class MainInfo {

	// designed so that it's a single iterable datacontainer (designed in purpose for frontend)
	public List<D2Arr> info = new ArrayList<D2Arr>();

	public void insert(String level2, String value){
		info.add(new D2Arr(level2, value));
	}
	
	public MainInfo(){}
	
	public MainInfo(String lvl2, String val){
		insert(lvl2, val);
	}
	

}

