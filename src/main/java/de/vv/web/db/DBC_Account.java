package de.vv.web.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.vv.web.model.RegistrationModel;
import de.vv.web.model.user.UserModel;
import de.vv.web.model.user.UserRoleModel;

//DataBaseConnection for Account
public class DBC_Account {

	public static UserModel registerUser(RegistrationModel user, String authority) {
		String queryString = "insert into vv_user (u_username, u_password, u_email) values (?,?,?);";
		try {
			if (isUserEmailAvailable(user.email)) {
				PreparedStatement ps = DBCon.con.prepareStatement(queryString);
				int psCount = 1;
				System.out.println(user);
				ps.setString(psCount++, user.username);
				ps.setString(psCount++, user.password);
				ps.setString(psCount++, user.email);
				ps.execute();
				UserModel um = getUserByEmail(user.email);
				System.out.println(um);
				if (um != null)
					return um;
				else
					return new UserModel(-1);
			} else
				return new UserModel(-2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new UserModel(-3);
	}

	public static UserModel getUserById(long id) {
		String queryString = "SELECT " // join query account with roles
				+ "dbo.vv_user.u_id," + "dbo.vv_user.u_username," + "dbo.vv_user.u_password," + "dbo.vv_user.u_email,"
				+ "dbo.vv_user.u_enabled," + "dbo.vv_authorities.aut_authority" + "	FROM dbo.vv_user, dbo.vv_authorities "
				+ "where dbo.vv_user.u_id=? " + "AND dbo.vv_user.u_id=dbo.vv_authorities.aut_user_id;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//int id, String username, String password, String email, boolean enabled, String authority
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername),
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail), rs.getBoolean(UserModel.dbEnabled),
						rs.getString(UserRoleModel.dbAuthority));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	String s = "SELECT u_id, u_username, u_password, u_email, u_email_verified FROM vv_user where u_email=?;";

	public static UserModel getUserByEmail(String email) {
		String queryString = "SELECT " + UserModel.dbId + ", " + UserModel.dbUsername + ", " + UserModel.dbPassword + ", "
				+ UserModel.dbEmail + ", " + UserModel.dbEnabled + " FROM vv_user where u_email=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//long id, String username, String password, String email, String role
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername),
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail), rs.getBoolean(UserModel.dbEnabled));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	String queryString = "SELECT count(*) as 'emailCount'	FROM dbo.vv_user where dbo.vv_user.u_email=?;";

	public static boolean isUserEmailAvailable(String email) {
		String queryString = "SELECT count(*) as 'emailCount'" + "	FROM dbo.vv_user " + "where dbo.vv_user.u_email=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// returnes true if db does not contain more than 0 same emails
				return rs.getInt("emailCount") == 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
