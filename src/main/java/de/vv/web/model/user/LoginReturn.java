package de.vv.web.model.user;

public class LoginReturn {
	public String message;
	public String username;
	public String email;
	public String token;
	
	public LoginReturn(){}
	
	public LoginReturn(String m){
		message = m;
	}
	
	public LoginReturn(String m, UserModel um, String tk){
		init(m, um.username, um.email, tk);
	}
	
	
	public LoginReturn(String m, String u, String e, String t){
		init(m,u,e,t);
	}
	
	void init(String m, String u, String e, String t){
		message = m;
		username = u;
		email = e;
		token = t;
	}
	
}
