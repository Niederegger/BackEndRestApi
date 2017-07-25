package de.vv.web.model.user;

public class UserRoleModel {
	public int id;
	public String role;
	
	public static String dbId = "ur_user_id";
	public static String dbAuthority = "ur_role";
	
	public UserRoleModel(){}
	
	public UserRoleModel(int id, String authority){
		this.id = id;
		this.role = authority;
	}
}
