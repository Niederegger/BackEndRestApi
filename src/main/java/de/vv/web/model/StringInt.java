package de.vv.web.model;

//simply contains a single string and int, addition
public class StringInt {
	public String string;
	public int integer;
	
	public StringInt(){}
	
	public StringInt(String s, int i){
		string = s;
		integer = i;
	}
	
	
	@Override
	public int hashCode() {
		return string.hashCode() * 3 + integer * 5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()) return false;
		StringInt o = (StringInt) obj;
		return o.integer == integer && o.string.equals(string);
	}
}
