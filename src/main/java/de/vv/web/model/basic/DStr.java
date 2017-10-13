package de.vv.web.model.basic;

public class DStr {
	public String str1 = "";
	public String str2 = "";

	public DStr(String s1, String s2) {
		str1 = s1;
		str2 = s2;
	}

	public DStr() {}
	
	@Override
	public String toString() {
		String s = "Str1: " + str1 + ", Str2: " + str2;
		return s;
	}
}
