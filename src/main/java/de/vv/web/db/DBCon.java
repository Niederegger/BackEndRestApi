package de.vv.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import de.vv.web.AjaxDemoApplication;
import de.vv.web.model.Account;
import de.vv.web.model.FileData;
import de.vv.web.model.MasterValue;
import de.vv.web.model.UserRole;
import de.vv.web.model.UserRoles;

public class DBCon {

	// ---------------------------------------------------------------------------------
	// DB Connection
	// ---------------------------------------------------------------------------------

	static Connection con;

	/**
	 * openin Connection to Ms Sql Database
	 */
	public static void openConnection() {
		SQLServerDataSource ds = new SQLServerDataSource();
		try {
			ds.setIntegratedSecurity(false);
			ds.setUser(AjaxDemoApplication.config.user);
			ds.setPassword(AjaxDemoApplication.config.pw);
			ds.setServerName(AjaxDemoApplication.config.serverName);
			ds.setPortNumber(AjaxDemoApplication.config.port);
			ds.setDatabaseName(AjaxDemoApplication.config.dbName);
			con = ds.getConnection();
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * closing connection
	 */
	public static void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------
	// Account
	// ---------------------------------------------------------------------------------

	public static void registerAccount(Account acc) {
		String queryString = "INSERT INTO [dbo].[vv_accounts]([acc_username],[acc_password],[acc_email],[acc_role])"
				+ " VALUES (?,?,?,?);";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			int psCount = 1;
			ps.setString(psCount++, acc.username);
			ps.setString(psCount++, acc.password);
			ps.setString(psCount++, acc.email);
			ps.setInt(psCount++, UserRoles.getRoleId(acc.role));
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Account getAccountById(long id) {
		String queryString = "SELECT " // join query account with roles
				+ "[dbo].[vv_accounts].[acc_id],"
				+ "[dbo].[vv_accounts].[acc_username],"
				+ "[dbo].[vv_accounts].[acc_password],"
				+ "[dbo].[vv_accounts].[acc_email],"
				+ "[dbo].[vv_user_roles].[UR_ROLE]"
				+"	FROM [dbo].[vv_accounts], [dbo].[vv_user_roles] "
				+"where [dbo].[vv_accounts].[acc_id]=? "
				+ "AND [dbo].[vv_accounts].[acc_role]=[dbo].[vv_user_roles].[UR_ID];";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//long id, String username, String password, String email, String role
				return new Account(rs.getLong("acc_id"), rs.getString("acc_username"), 
						rs.getString("acc_password"), rs.getString("acc_email"), rs.getString("UR_ROLE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isAccountEmailAvailable(String email) {
		String queryString = "SELECT count(*) as 'emailCount'"
				+"	FROM [dbo].[vv_accounts] "
				+"where [dbo].[vv_accounts].[acc_email]=?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// returnes true if db does not contain more than 0 same emails
				return !(rs.getInt("emailCount") >= 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static Account getAccountByEmail(String email) {
		String queryString = "SELECT " // join query account with roles
				+ "[dbo].[vv_accounts].[acc_id],"
				+ "[dbo].[vv_accounts].[acc_username],"
				+ "[dbo].[vv_accounts].[acc_password],"
				+ "[dbo].[vv_accounts].[acc_email],"
				+ "[dbo].[vv_user_roles].[UR_ROLE]"
				+"	FROM [dbo].[vv_accounts], [dbo].[vv_user_roles] "
				+"where [dbo].[vv_accounts].[acc_email]=? "
				+ "AND [dbo].[vv_accounts].[acc_role]=[dbo].[vv_user_roles].[UR_ID];";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//long id, String username, String password, String email, String role
				return new Account(rs.getLong("acc_id"), rs.getString("acc_username"), 
						rs.getString("acc_password"), rs.getString("acc_email"), rs.getString("UR_ROLE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getRole(String role){
		String queryString = "select [UR_ID] from [vv_user_roles] where ur_role=?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, role);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("UR_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // invalid role
	}
	
	public static List<UserRole> getAllRoles(){
		String queryString = "select [UR_ID], [UR_ROLE] from [vv_user_roles];";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ResultSet rs = ps.executeQuery();
			List<UserRole> lur = new ArrayList<UserRole>();
			while (rs.next()) {
				lur.add(new UserRole(rs.getInt("UR_ID"), rs.getString("UR_ROLE")));
			}
			return lur;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // invalid role
	}

	// ---------------------------------------------------------------------------------
	// File Server
	// ---------------------------------------------------------------------------------

	public static int fileUploadEntry(FileData s) {
		String queryString = "INSERT INTO [dbo].[vv_fileserver] ([fs_filename], [fs_location], [fs_fk_user]) VALUES (?, ?, ?);";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, s.filename);
			ps.setString(2, s.location);
			ps.setInt(3, s.userId);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * supposed to fetch all files from fileserver which are like name
	 * 
	 * @param isin
	 * @return
	 */
	public static List<String> getAllFiles(String name) {
		String queryString = "select fs_filename from vv_fileserver where  fs_filename like ?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();
			List<String> fileList = new ArrayList<String>();
			while (rs.next()) {
				fileList.add(rs.getString("fs_filename").trim());
			}
			return fileList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returnes file location
	 * 
	 * @param isin
	 * @return
	 */
	public static String getFileLocation(String name) {
		String queryString = "select fs_location from vv_fileserver where  fs_filename=?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			// List<String> fileList = new ArrayList<String>();
			while (rs.next()) {
				return rs.getString("fs_location").trim();
				// fileList.add(rs.getString("fs_location").trim());
			}
			// return fileList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------
	// MasterValues
	// ---------------------------------------------------------------------------------

	/**
	 * supposed to fetch all isin from mastervalues which are like isin
	 * 
	 * @param isin
	 * @return
	 */
	public static List<String> getAllIsins(String isin) {
		String queryString = "select distinct mv_isin from vv_mastervalues where  mv_isin like ?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, "%" + isin + "%");
			ResultSet rs = ps.executeQuery();
			List<String> isinList = new ArrayList<String>();
			while (rs.next()) {
				isinList.add(rs.getString("MV_ISIN"));
			}
			return isinList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * fetching isin mastervalues
	 * 
	 * @param isin
	 * @return
	 */
	public static List<MasterValue> getIsinData(String isin) {
		// bei preparedStatements einfach ein ? und keine '?' <-- fehlerquelle
		String isw = "SELECT [MV_SOURCE_ID] ,[MV_UPLOAD_ID] ,[MV_ISIN] ,[MV_MIC] ,[MV_AS_OF_DATE] ,[MV_FIELDNAME] ,[MV_TIMESTAMP] ,[MV_STRINGVALUE] ,[MV_DATA_ORIGIN] ,[MV_URLSOURCE] ,[MV_COMMENT] FROM [dbo].[vv_mastervalues] WHERE [MV_ISIN]=?";
		try {
			PreparedStatement ps = con.prepareStatement(isw);
			ps.setString(1, isin);
			ResultSet rs = ps.executeQuery();
			List<MasterValue> lmv = new ArrayList<MasterValue>();
			while (rs.next()) {
				// lade die daten aus dem ResultSet
				// die Daten werdeng etrimmed um unnoetige whitespaces zu cutten
				String mvIsin = rs.getString("MV_ISIN");
				if (mvIsin != null)
					mvIsin = mvIsin.trim();
				String mvSourceId = rs.getString("MV_SOURCE_ID");
				if (mvSourceId != null)
					mvSourceId = mvSourceId.trim();
				String mvAOD = rs.getString("MV_AS_OF_DATE");
				if (mvAOD != null)
					mvAOD = mvAOD.trim();
				String mvFN = rs.getString("MV_FIELDNAME");
				if (mvFN != null)
					mvFN = mvFN.trim();
				String mvTS = rs.getString("MV_TIMESTAMP");
				if (mvTS != null)
					mvTS = mvTS.trim();
				String mvVal = rs.getString("MV_StringVALUE");
				if (mvVal != null)
					mvVal = mvVal.trim();
				String mvDO = rs.getString("MV_DATA_ORIGIN");
				if (mvDO != null)
					mvDO = mvDO.trim();
				String mvUS = rs.getString("MV_URLSOURCE");
				if (mvUS != null)
					mvUS = mvUS.trim();
				String mvC = rs.getString("MV_COMMENT");
				if (mvC != null)
					mvC = mvC.trim();
				String mvMic = rs.getString("MV_MIC");
				if (mvMic != null)
					mvMic = mvMic.trim();
				String mvUI = rs.getString("MV_UPLOAD_ID");
				if (mvUI != null)
					mvUI = mvUI.trim();
				lmv.add(new MasterValue(mvSourceId, mvIsin, mvAOD, mvFN, mvTS, mvVal, mvDO, mvUS, mvC, mvMic, mvUI));
			}
			return lmv;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
