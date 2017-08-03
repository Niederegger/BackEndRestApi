package de.vv.web.model.maininfo;

import de.vv.web.model.StringInt;

public class QuellenEntry {
	public String lvl2;
	public String val;
	public int sqn;
	public int srcn;
	public String url;
	public String src;
	public StringInt[] quellen;

	public QuellenEntry(){}
	
	public QuellenEntry(String a, String b, int c, int c2, String d, String e, StringInt[] q){
		lvl2 = a;
		val = b;
		sqn = c;
		srcn = c2;
		url = d;
		src = e;
		quellen = q;
	}
	
}
