package de.vv.web.model;

public class Account {
	public long id;
	public String username;
	public String password;
	public String email;
	public String role;
	
	public Account(){}
	
	public Account(String username, String password, String email, String role){
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}
	
	public Account(long id, String username, String password, String email, String role){
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}
}
