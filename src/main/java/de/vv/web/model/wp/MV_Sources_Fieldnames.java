package de.vv.web.model.wp;

import java.util.HashMap;

import de.vv.web.model.basic.BStr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MV_Sources_Fieldnames {
	public HashMap<String,ArrayList<BStr>> values = new HashMap<String, ArrayList<BStr>>();
	public ArrayList<String> keyOrder = new ArrayList<String>();
	
	
	public MV_Sources_Fieldnames(){}
	
	public MV_Sources_Fieldnames(ResultSet rs){init(rs);}
	
	public void init(ResultSet rs){
		try {
			String fn;
			String src;
			ArrayList<BStr> tmp;
			while(rs.next()){
				fn = rs.getString("MV_FIELDNAME");
				if(fn!=null)fn=fn.trim();
				src = rs.getString("MV_SOURCE_ID");
				if(src!=null)src=src.trim();
				if(values.containsKey(src)){
					tmp = values.get(src);
				} else {
					keyOrder.add(src);
					tmp = new ArrayList<BStr>();
				}
				tmp.add(new BStr(fn, false));
				values.put(src, tmp);				
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
}
