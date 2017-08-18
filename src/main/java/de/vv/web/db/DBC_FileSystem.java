package de.vv.web.db;

import java.sql.CallableStatement;
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
		try {
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_insert_file_entry (?, ?, ?, ?, ?, ?, ?, ?)}");
			int count = 1;
			cstmt.setString(count++, s.filename);
			cstmt.setString(count++, s.location);
			cstmt.setInt(count++, s.userId);
			cstmt.setString(count++, s.isin);
			cstmt.setString(count++, s.ip);
			cstmt.setString(count++, s.dataOrigin);
			cstmt.setString(count++, s.dataType);
			cstmt.setString(count++, s.comment);

			cstmt.execute();
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
		try {
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_delete_file_entry (?)}");
			cstmt.setString(1, location);
			cstmt.execute();
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
		try {
			FileModelContainer fm = new FileModelContainer();
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_get_file_entries (?)}");
			cstmt.setString(1, isin);
			ResultSet rs = cstmt.executeQuery();
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
		try {
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_get_file_location (?, ?)}");
			cstmt.setString(1, fileName);
			cstmt.setString(2, timeStamp);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				return rs.getString("fs_location").trim();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
