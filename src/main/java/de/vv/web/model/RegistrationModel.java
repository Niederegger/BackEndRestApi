package de.vv.web.model;

import de.vv.web.functions.BasicFunctions;

// used for registration
public class RegistrationModel {
	public String username;
	public String email;
	public String password;
	
	public RegistrationModel(){}
	
	public RegistrationModel(String username, String email, String password){
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * check whether inputs are valid
	 * 
	 * @return
	 */
	public boolean check() {
		if (username != null && email != null && password != null) {
			boolean valid = username.length() > 2 && username.length() < 32;
			valid &= BasicFunctions.validateEmail(email);
			valid &= password.length() > 5 && password.length() < 32;
			return  valid;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Username: ");
		sb.append(username);
		sb.append("\nEmail: ");
		sb.append(email);
		sb.append("\nPassword: ");
		sb.append(password);
		sb.append("\n");
		return sb.toString();
	}
}
