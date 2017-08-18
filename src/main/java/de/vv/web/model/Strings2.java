package de.vv.web.model;

public class Strings2 {
	public String str1 = "";
	public String str2 = "";

	public Strings2(String s1, String s2) {
		str1 = s1;
		str2 = s2;
	}

	
	public Strings2() {}
	
	
	@Override
	public String toString() {
		String s = "Str1: " + str1 + ", Str2: " + str2;
		return s;
	}
}
