package de.vv.web.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FieldModel {
	public HashMap<String, ArrayList<Strings2>> container = new HashMap<String, ArrayList<Strings2>>();
	public ArrayList<String> keyOrder = new ArrayList<String>();
	
	public FieldModel(ResultSet rs, String f1){
		init(rs, f1);
	}
	
	public FieldModel(){}
	
	
	public void init(ResultSet rs, String f1){
		try {
			String s;
			String s2;
			String[] tmp;
			ArrayList<Strings2> als;
			while(rs.next()){
				s = rs.getString(f1);
				if(s!=null)s=s.trim();
				// normalize
				if (s != null && s.contains(".")) {
					tmp = s.split("\\.");
					s = tmp[0];
					s2 = tmp[1];
				} else s2 = null;
				// add to cotnainer
				if(keyOrder.contains(s)){
					als = container.get(s);
					als.add(new Strings2(s2, ""));
					container.put(s, als);
				} else {
					keyOrder.add(s);
					als = new ArrayList<Strings2>();
					als.add(new Strings2(s2==null?"":s2, ""));
					container.put(s, als);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void reduce(){
		boolean val = false;
		ArrayList<Strings2> cont;
		for(String k : container.keySet()){
			cont = container.get(k);
			if(cont == null || cont.size() == 0 || k.toLowerCase().equals("isin")){
				container.remove(k);
			} else {
				Iterator<Strings2> i = cont.iterator();
				val = false;
				while (i.hasNext()) {
					Strings2 s2 = i.next(); // must be called before you can call i.remove()
					if(s2.str2 == null || s2.str2.equals("") || s2.str2.isEmpty())
				   i.remove();
					else val = true;
				}
				if(!val) container.remove(k);
			}
		}
	}
	
	public void clean(ArrayList<String> def){
		for(String k : container.keySet()){
			if(!def.contains(k))
				container.remove(k);
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String k : keyOrder){
			sb.append(k);
			sb.append(":\n");
			for(Strings2 s2 : container.get(k)){
				sb.append(s2.toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String getIsin(){
		if(container != null){
			ArrayList<Strings2> l = container.get("ISIN");
			if(l != null && l.size() > 0){
				return l.get(0).str2;
			}
		}
		return null;
	}
}
