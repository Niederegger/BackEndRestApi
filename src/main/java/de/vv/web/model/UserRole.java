package de.vv.web.model;

/**
 * represents a single user role
 * 
 * @author Alexey Gasevic
 *
 */
public class UserRole {
	public int id;
	public String role;

	public UserRole(int id, String role) {
		this.id = id;
		this.role = role;
	}
}
