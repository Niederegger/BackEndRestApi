package de.vv.web.model;

import java.util.HashMap;
import java.util.List;

import de.vv.web.db.DBCon;

/**
 * represents all user roles
 * 
 * @author Alexey Gasevic
 *
 */
public class UserRoles {
	public static List<UserRole> listUserRoles;
	public static HashMap<String, Integer> roles = new HashMap<String, Integer>();

	public static void loadRoles() {
		UserRoles.listUserRoles = DBCon.getAllRoles();
		for (UserRole ur : listUserRoles) {
			UserRoles.roles.put(ur.role, ur.id);
		}
	}

	public static int getRoleId(String role) {
		if (roles.containsKey(role))
			return roles.get(role);
		else
			return -1;
	}
}
