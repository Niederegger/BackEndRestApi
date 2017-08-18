package de.vv.web.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import de.vv.web.App;

public class DBCon {

	static Connection con;
	static CallableStatement csMainInfo2;
	static CallableStatement csIsinForWkn;
	static CallableStatement csDeleteFileEntry;
	static CallableStatement csGetFileEntry;
	static CallableStatement csGetFileLocation;

	/**
	 * opening Connection to Ms SQL Database
	 */
	public static void openConnection() {
		SQLServerDataSource ds = new SQLServerDataSource();
		try {
			ds.setIntegratedSecurity(false);
			ds.setUser(App.config.user);
			ds.setPassword(App.config.pw);
			ds.setServerName(App.config.serverName);
			ds.setPortNumber(App.config.port);
			ds.setDatabaseName(App.config.dbName);
			con = ds.getConnection();
		}
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
	
	public static void initCallableStatements(){
		try {
			// WP
			csMainInfo2 = con.prepareCall ("{call vvsp_get_maininfoV2 (?)}");
			csIsinForWkn = con.prepareCall ("{call vvsp_get_isin_for_wkn (?)}");
			// fileserver
			csDeleteFileEntry = con.prepareCall ("{call vvsp_delete_file_entry (?)}");
			csGetFileEntry = con.prepareCall ("{call vvsp_get_file_entries (?)}");
			csGetFileLocation = con.prepareCall ("{call vvsp_get_file_location (?, ?)}");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeCallableStatements(){
		try {
			csMainInfo2.close();// = con.prepareCall ("{call vvsp_get_maininfoV2 (?)}");
			csIsinForWkn.close();// = con.prepareCall ("{call vvsp_get_isin_for_wkn (?)}");
			csDeleteFileEntry.close();// = con.prepareCall ("{call vvsp_delete_file_entry (?)}");
			csGetFileEntry.close();// = con.prepareCall ("{call vvsp_get_file_entries (?)}");
			csGetFileLocation.close();// = con.prepareCall ("{call vvsp_get_file_location (?)}");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
