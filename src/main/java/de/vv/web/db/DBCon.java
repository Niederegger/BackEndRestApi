package de.vv.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import de.vv.web.AjaxDemoApplication;
import de.vv.web.model.MasterValue;

public class DBCon {

	static Connection con;
	static String user = "TestUser";
	static String pw = "TestUser";
	static String serverName = "ACER-2016\\SQLEXPRESS";
	static String dbName = "MasterData";
	static int port = 1433;

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
	 * supposed to fetch all isin from mastervalues
	 * which contains input isin
	 * @param isin
	 * @return
	 */
	public static List<String> getAllIsins(String isin){
		String queryString = "select distinct mv_isin from vv_mastervalues where  mv_isin like ?;";
		try {
			PreparedStatement ps = con.prepareStatement(queryString);
			ps.setString(1, "%"+isin+"%");
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
				if(mvIsin != null)mvIsin=mvIsin.trim();
				String mvSourceId = rs.getString("MV_SOURCE_ID");
				if(mvSourceId != null)mvSourceId=mvSourceId.trim();
				String mvAOD = rs.getString("MV_AS_OF_DATE");
				if(mvAOD != null)mvAOD=mvAOD.trim();
				String mvFN = rs.getString("MV_FIELDNAME");
				if(mvFN != null)mvFN=mvFN.trim();
				String mvTS = rs.getString("MV_TIMESTAMP");
				if(mvTS != null)mvTS=mvTS.trim();
				String mvVal = rs.getString("MV_StringVALUE");
				if(mvVal != null)mvVal=mvVal.trim();
				String mvDO = rs.getString("MV_DATA_ORIGIN");
				if(mvDO != null)mvDO=mvDO.trim();
				String mvUS = rs.getString("MV_URLSOURCE");
				if(mvUS != null)mvUS=mvUS.trim();
				String mvC = rs.getString("MV_COMMENT");
				if(mvC != null)mvC=mvC.trim();
				String mvMic = rs.getString("MV_MIC");
				if(mvMic != null)mvMic=mvMic.trim();
				String mvUI = rs.getString("MV_UPLOAD_ID");
				if(mvUI != null)mvUI=mvUI.trim();
				lmv.add(new MasterValue(mvSourceId, mvIsin, mvAOD, mvFN, mvTS, mvVal, mvDO, mvUS, mvC, mvMic, mvUI));
			}
			return lmv;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
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
}
