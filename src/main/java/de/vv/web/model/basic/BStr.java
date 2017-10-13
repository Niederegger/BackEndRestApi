package de.vv.web.model.basic;

public class BStr {
	public String str = "";
	public boolean bool = false;

	public BStr(String s, boolean b) {
		str = s;
		bool = b;
	}

	public BStr() {}
	
	@Override
	public String toString() {
		String s = "str: " + str + ", bool: " + bool;
		return s;
	}
}
