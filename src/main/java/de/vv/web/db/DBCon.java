package de.vv.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import de.vv.web.AjaxDemoApplication;
import de.vv.web.model.*;

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

	public static UserModel registerUser(RegistrationModel user, String authority) {
		String queryString = "insert into vv_user (u_username, u_password, u_email) values (?,?,?);";
		try {
			if(isUserEmailAvailable(user.email)){
				PreparedStatement ps = con.prepareStatement(queryString);
				int psCount = 1;
				System.out.println(user);
				ps.setString(psCount++, user.username);
				ps.setString(psCount++, user.password);
				ps.setString(psCount++, user.email);
				ps.execute();
				UserModel um = getUserByEmail(user.email);
				System.out.println(um);
				if(um !=null) return um;
				else return new UserModel(-1);
			} else return new UserModel(-2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new UserModel(-3);
	}

	public static UserModel getUserById(long id) {
		String queryString = "SELECT " // join query account with roles
				+ "dbo.vv_user.u_id,"
				+ "dbo.vv_user.u_username,"
				+ "dbo.vv_user.u_password,"
				+ "dbo.vv_user.u_email,"
				+ "dbo.vv_user.u_enabled,"
				+ "dbo.vv_authorities.aut_authority"
				+"	FROM dbo.vv_user, dbo.vv_authorities "
				+"where dbo.vv_user.u_id=? "
				+ "AND dbo.vv_user.u_id=dbo.vv_authorities.aut_user_id;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//int id, String username, String password, String email, boolean enabled, String authority
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername), 
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail), 
						rs.getBoolean(UserModel.dbEnabled), rs.getString(UserRoleModel.dbAuthority));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static UserModel getUserByEmail(String email) {
		String queryString = "SELECT "+UserModel.dbId+", "+UserModel.dbUsername+", "+UserModel.dbPassword
				+", "+UserModel.dbEmail+", "+UserModel.dbEnabled+" FROM vv_user where u_email=? ";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//long id, String username, String password, String email, String role
				return new UserModel(rs.getInt(UserModel.dbId), rs.getString(UserModel.dbUsername), 
						rs.getString(UserModel.dbPassword), rs.getString(UserModel.dbEmail), 
						rs.getBoolean(UserModel.dbEnabled));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isUserEmailAvailable(String email) {
		String queryString = "SELECT count(*) as 'emailCount'"
				+"	FROM dbo.vv_user "
				+"where dbo.vv_user.u_email=?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
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

	// ---------------------------------------------------------------------------------
	// File Server
	// ---------------------------------------------------------------------------------

	public static int fileUploadEntry(FileData s) {
		String queryString = "INSERT INTO dbo.vv_fileserver (fs_filename, fs_location, fs_fk_user, fs_establishment, fs_ip, fs_comment) VALUES (?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			int count = 1;
			ps.setString(count++, s.filename);
			ps.setString(count++, s.location);
			ps.setInt(count++, s.userId);
			ps.setString(count++, s.establishment);
			ps.setString(count++, s.ip);
			ps.setString(count++, s.comment);
			
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
	public static FileModelContainer getAllFiles() {
		try {
			FileModelContainer fm = new FileModelContainer();
			PreparedStatement ps = con.prepareStatement(fm.queryString);
			ResultSet rs = ps.executeQuery();
			fm.fill(rs);
			return fm;
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
	public static String getFileLocation(String fileName, String timeStamp) {
		String queryString = "select fs_location from vv_fileserver where  fs_filename=? and fs_timestamp=?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, fileName);
			ps.setString(2, timeStamp);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("fs_location").trim();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------
	// MasterValues
	// ---------------------------------------------------------------------------------


	public static void uplaodData(UploadContainer data){
		try {
			PreparedStatement ps;
			int stmtCount;
			// patching edit
			for(int i = 0; i < data.fieldName.size(); i++){
				// (MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT)
				ps = con.prepareStatement(data.query);
				stmtCount = 1;
				ps.setString(stmtCount++, data.origin); // Data Origin
				ps.setString(stmtCount++, data.source);
				ps.setString(stmtCount++, data.isin);
				ps.setString(stmtCount++, data.mic);
				ps.setString(stmtCount++, data.fieldName.get(i));
				ps.setString(stmtCount++, data.values.get(i));
				ps.setString(stmtCount++, data.comment);
				ps.executeUpdate();
			}
			// executing upload to main table
			stmtCount = 1;
			ps = con.prepareStatement("exec vvsp_import_uploadV2 ?, ?, ?, ?;");
			ps.setString(stmtCount++, data.source); // SourceId
			ps.setString(stmtCount++, data.origin); // Data Origin
			ps.setString(stmtCount++, data.urlSource); // UrlSource
			ps.setString(stmtCount++, data.comment); // Comment
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean doesIsinExist(String isin){
		if(isin.length() == 12){
			String queryString = "select top (1) mv_isin from vv_mastervalues where mv_isin=?;";
			try {
				PreparedStatement ps = con.prepareStatement(queryString);
				ps.setString(1, isin);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return (isin.toLowerCase().equals(rs.getString("mv_isin").toLowerCase()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static MainInfoContainer getMainInfo(String isin) {
		String queryString = "exec vvsp_get_maininfo ?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, isin);
			System.out.println("exec vvsp_get_maininfo '"+isin+"';");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			int count = 0;
			while(rs == null) {
				count ++;
				ps.getMoreResults();
				rs = ps.getResultSet();
				// stored procedure return multiple result-sets,
				// therefore one has to loop through each null set,
				// finally reaching the right on, 
				// as security a lock has been set at 100 loops
				if(count++ >100) return null; 
			}
			MainInfoContainer mi = new MainInfoContainer();
			while (rs.next()) {
				mi.instert(rs.getString("LEVEL1"), rs.getString("LEVEL2"), rs.getString("STRINGVALUE"));
			}
			return mi;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	public static IsinData getIsinInfo(String v) {
		try {
			PreparedStatement ps = con.prepareStatement(IsinData.query);
			ps.setString(1, v);
			ResultSet rs = ps.executeQuery();
			IsinData data = new IsinData();
			while (rs.next()) {
				data.add(IdRow.parse(rs));
			}
			data.sort();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
	 * supposed to fetch all isin from mastervalues which are like isin
	 * 
	 * @param isin
	 * @return
	 */
	public static List<String> getIsinList() {
		String queryString = "select distinct mv_isin from vv_mastervalues;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
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
		String isw = "SELECT MV_SOURCE_ID ,MV_UPLOAD_ID ,MV_ISIN ,MV_MIC ,MV_AS_OF_DATE ,MV_FIELDNAME ,MV_TIMESTAMP ,MV_STRINGVALUE ,MV_DATA_ORIGIN ,MV_URLSOURCE ,MV_COMMENT FROM dbo.vv_mastervalues WHERE MV_ISIN=?";
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
	
	public static List<WPDModel> getWpd(String isin) {
		String isw = "SELECT MV_SOURCE_ID ,MV_ISIN ,MV_STRINGVALUE FROM dbo.vv_mastervalues where  mv_isin like ? and MV_FIELDNAME='hyperlink';";
		try {
			PreparedStatement ps = con.prepareStatement(isw);
			ps.setString(1, "%" + isin + "%");
			ResultSet rs = ps.executeQuery();
			List<WPDModel> list = new ArrayList<WPDModel>();
			while (rs.next()) {
				// lade die daten aus dem ResultSet
				// die Daten werdeng etrimmed um unnoetige whitespaces zu cutten
				String mvIsin = rs.getString("MV_ISIN");
				if (mvIsin != null)
					mvIsin = mvIsin.trim();
				String mvSourceId = rs.getString("MV_SOURCE_ID");
				if (mvSourceId != null)
					mvSourceId = mvSourceId.trim();
				String mvVal = rs.getString("MV_StringVALUE");
				if (mvVal != null)
					mvVal = mvVal.trim();
				list.add(new WPDModel(mvSourceId, mvVal, mvIsin));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
