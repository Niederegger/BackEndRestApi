package de.vv.web.model;

/**
 * simple container for 2 String Mapping more of a hack then aestethics
 * 
 * @author Alexey Gasevic
 *
 */
public class D2Arr {
	public D2Arr(String l, String v) {
		lvl2 = l == null ? "" : l;
		val = v == null ? "" : v;
	}

	public String lvl2;
	public String val;
}