package de.vv.web.model;

public class UserModel {
	//---------------------------------------
	// ErrorCodes: (stored in id)
	//---------------------------------------
	// -1 = registration failed
	// -2 = Email unavailable
	//
	//---------------------------------------
	
	
	public int id;
	public String username;
	public String password;
	public String email;
	public boolean enabled;
	public String authority;
	
	public static String dbId = "u_id";
	public static String dbUsername = "u_username";
	public static String dbPassword = "u_password";
	public static String dbEmail = "u_email";
	public static String dbEnabled = "u_enabled";
	
	public UserModel(){}
	
	public UserModel(int id){
		this.id = id;
	}
	
	public UserModel(String username, String password, String email, boolean enabled){
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
	}
	
	public UserModel(String username, String password, String email, boolean enabled, String authority){
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.authority = authority;
	}
	
	public UserModel(int id, String username, String password, String email, boolean enabled){
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
	}
	
	public UserModel(int id, String username, String password, String email, boolean enabled, String authority){
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.authority = authority;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Username: ");
		sb.append(username);
		sb.append("\nId: ");
		sb.append(id);
		sb.append("\nEmail: ");
		sb.append(email);
		sb.append("\nPassword: ");
		sb.append(password);
		sb.append("\nenabled: ");
		sb.append(enabled);
		sb.append("\nauthority: ");
		sb.append(authority);
		sb.append("\n");
		return sb.toString();
	}
}
