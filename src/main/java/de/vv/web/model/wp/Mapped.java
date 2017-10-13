package de.vv.web.model.wp;

public class Mapped {
	public String source;
	public String fieldname;
	public String mapname;
	
	
	public Mapped(){}
	
	public Mapped(String src, String fn, String mfn){
		source = src;
		fieldname = fn;
		mapname = mfn;
	}
}
