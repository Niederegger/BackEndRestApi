package de.vv.web.model;

public class AuthorityModel {
	public int id;
	public String authority;
	
	public static String dbId = "aut_user_id";
	public static String dbAuthority = "aut_authority";
	
	public AuthorityModel(){}
	
	public AuthorityModel(int id, String authority){
		this.id = id;
		this.authority = authority;
	}
}
