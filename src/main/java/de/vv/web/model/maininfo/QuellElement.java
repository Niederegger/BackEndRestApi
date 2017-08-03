package de.vv.web.model.maininfo;

public class QuellElement {
	public QuellenEntry qe;
	public String key;
	
	public QuellElement(String key, QuellenEntry qe){
		this.key = key; this.qe = qe;
	}
}
