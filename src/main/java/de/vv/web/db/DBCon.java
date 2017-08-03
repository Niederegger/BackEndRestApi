package de.vv.web.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import de.vv.web.App;

public class DBCon {

	static Connection con;

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

}
