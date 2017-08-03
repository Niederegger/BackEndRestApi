package de.vv.web.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.vv.web.model.files.FileData;
import de.vv.web.model.files.FileModelContainer;

//DataBaseConnection for FileSystem
public class DBC_FileSystem {

	/**
	 * creates an FileEntry
	 * 
	 * @param s
	 *          FileData
	 * @return Error/SuccessCode
	 */
	public static int fileUploadEntry(FileData s) {
		String queryString = "INSERT INTO dbo.vv_fileserver (fs_filename, fs_location, fs_fk_user, fs_isin, fs_ip, fs_comment, fs_data_origin, fs_data_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			int count = 1;
			ps.setString(count++, s.filename);
			ps.setString(count++, s.location);
			ps.setInt(count++, s.userId);
			ps.setString(count++, s.isin);
			ps.setString(count++, s.ip);
			ps.setString(count++, s.comment);
			ps.setString(count++, s.dataOrigin);
			ps.setString(count++, s.dataType);

			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * removes FileEntry
	 * 
	 * @param location
	 *          unique location of File
	 * @return true/false
	 */
	public static boolean removeFileEntry(String location) {
		String queryString = "delete from vv_fileserver where fs_location=?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, location);
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * supposed to fetch all files from fileserver which are like name
	 * 
	 * @param isin
	 * @return
	 */
	public static FileModelContainer getFiles(String isin) {
		String query = "select * from vv_fileserver where fs_isin=?;";
		try {
			FileModelContainer fm = new FileModelContainer();
			PreparedStatement ps = DBCon.con.prepareStatement(query);
			ps.setString(1, isin);
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
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
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

}
