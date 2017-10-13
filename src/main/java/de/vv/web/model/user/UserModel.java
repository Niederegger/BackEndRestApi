package de.vv.web.model.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.validator.constraints.NotEmpty;

import de.vv.web.functions.BF;

public class UserModel {
	// ---------------------------------------
	// ErrorCodes: (stored in id)
	// ---------------------------------------
	// -1 = registration failed
	// -2 = Email unavailable
	//
	// ---------------------------------------

	public int id;
	@NotEmpty
	public String username;
	public String password;
	@NotEmpty
	public String email;
	public boolean enabled;
	public String role;
	public String confirmationToken;

	public static String dbId = "u_id";
	public static String dbUsername = "u_username";
	public static String dbPassword = "u_password";
	public static String dbEmail = "u_email";
	public static String dbEnabled = "u_enabled";
	public static String dbConfirmationToken = "u_confirmationToken";

	public UserModel() {
	}

	public UserModel(int id) {
		this.id = id;
	}

	public UserModel(ResultSet rs) {
		try {
			id = rs.getInt(dbId);
			username = BF.trimming(rs.getString(dbUsername));
			password = BF.trimming(rs.getString(dbPassword));
			email = BF.trimming(rs.getString(dbEmail));
			confirmationToken = BF.trimming(rs.getString(dbConfirmationToken));
			enabled = rs.getBoolean(dbEnabled);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public UserModel(String username, String password, String email, boolean enabled) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
	}

	public UserModel(String username, String password, String email, boolean enabled, String authority) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.role = authority;
	}

	public UserModel(int id, String username, String password, String email, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
	}

	public UserModel(int id, String username, String password, String email, boolean enabled, String authority) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.role = authority;
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
		sb.append(role);
		sb.append("\n");
		return sb.toString();
	}
}
