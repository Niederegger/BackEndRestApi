package de.vv.web.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.vv.web.model.RegistrationModel;
import de.vv.web.model.user.UserModel;
import de.vv.web.model.user.UserRoleModel;

//DataBaseConnection for Account
public class DBC_User {

	public static int toggleUserEnabledByToken(String token, boolean enabled){
		String queryString ="update vv_user set u_enabled=? where u_confirmationToken=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setBoolean(1, enabled);
			ps.setString(2, token);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return -1;
	}
	
	public static int toggleUserEnabledByMail(String mail, boolean enabled){
		String queryString ="update vv_user set u_enabled=? where u_email=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setBoolean(1, enabled);
			ps.setString(2, mail);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return -1;
	}
	
	public static UserModel findByEmail(String email) {
		String queryString = "select  * from vv_user where " + UserModel.dbEmail + "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}
	
	public static UserModel findByID(int id) {
		String queryString = "select  * from vv_user where " + UserModel.dbId + "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	public static UserModel fetchUserByEmailAndName(String mail, String name) {
		String queryString = "select  * from vv_user where " + UserModel.dbEmail + "=? or " + UserModel.dbUsername
				+ "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, mail);
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	public static UserModel fetchUser(String mail, String name, String token) {
		String queryString = "select  * from vv_user where " + UserModel.dbEmail + "=? and " + UserModel.dbUsername
				+ "=? and " + UserModel.dbConfirmationToken + "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, mail);
			ps.setString(2, name);
			ps.setString(3, token);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	public static UserModel findByName(String name) {
		String queryString = "select  * from vv_user where " + UserModel.dbUsername + "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	public static UserModel findByToken(String token) {
		String queryString = "select  * from vv_user where " + UserModel.dbConfirmationToken + "=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, token);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new UserModel(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	// user.username, user.email, user.password, confirmationToken
	public static void saveUser(String name, String email, String password, String token) {
		String queryString = "insert into vv_user (" + UserModel.dbUsername + ", " + UserModel.dbPassword + ", "
				+ UserModel.dbEmail + ", " + UserModel.dbConfirmationToken + ") values (?,?,?,?);";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			int psCount = 1;
			ps.setString(psCount++, name);
			ps.setString(psCount++, password);
			ps.setString(psCount++, email);
			ps.setString(psCount++, token);

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
	}

	public static void validateUser(UserModel um, boolean val) {
		String queryString = "update vv_user set u_enabled=? where u_email=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setBoolean(0, val);
			ps.setString(1, um.email);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
	}

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
			DBCon.checkCon();
		}
		return new UserModel(-3);
	}

	public static UserModel getUserById(long id) {
		String queryString = "SELECT " // join query account with roles
				+ "dbo.vv_user.u_id," + "dbo.vv_user.u_username," + "dbo.vv_user.u_password," + "dbo.vv_user.u_email,"
				+ "dbo.vv_user.u_enabled," + "dbo.vv_authorities.aut_authority"
				+ "	FROM dbo.vv_user, dbo.vv_authorities " + "where dbo.vv_user.u_id=? "
				+ "AND dbo.vv_user.u_id=dbo.vv_authorities.aut_user_id;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// int id, String username, String password, String email,
				// boolean enabled, String authority
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername),
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail),
						rs.getBoolean(UserModel.dbEnabled), rs.getString(UserRoleModel.dbAuthority));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
	}

	String s = "SELECT u_id, u_username, u_password, u_email, u_email_verified FROM vv_user where u_email=?;";

	public static UserModel getUserByEmail(String email) {
		String queryString = "SELECT " + UserModel.dbId + ", " + UserModel.dbUsername + ", " + UserModel.dbPassword
				+ ", " + UserModel.dbEmail + ", " + UserModel.dbEnabled + " FROM vv_user where u_email=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// long id, String username, String password, String email,
				// String role
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername),
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail),
						rs.getBoolean(UserModel.dbEnabled));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
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
			DBCon.checkCon();
		}
		return true;
	}

}
