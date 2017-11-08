package de.vv.web.db;

import java.sql.CallableStatement;
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
			DBCon.checkCon();
		}
		return -1;
	}
	
	public static String fileUploadEntry(
			String filename,
			String location,
			int userId,
			String isin,
			String ip,
			String dataOrigin,
			String dataType,
			String comment			
			) {
		try {
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_insert_file_entry (?, ?, ?, ?, ?, ?, ?, ?)}");
			int count = 1;
			cstmt.setString(count++, filename);
			cstmt.setString(count++, location);
			cstmt.setInt(count++, userId);
			cstmt.setString(count++, isin);
			cstmt.setString(count++, ip);
			cstmt.setString(count++, dataOrigin);
			cstmt.setString(count++, dataType);
			cstmt.setString(count++, comment);

			cstmt.execute();
			PreparedStatement ps = DBCon.con.prepareStatement("select fs_timestamp from vv_fileserver where fs_isin=? and "
					+ "fs_filename=? and fs_location=? and fs_fk_user=?");
			ps.setString(1, isin);
			ps.setString(2, filename);
			ps.setString(3, location);
			ps.setInt(4, userId);

			ResultSet rs = ps.executeQuery();
			if(rs.next()) return rs.getString("fs_timestamp");
			return "none";
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			DBCon.checkCon();
		}
		return null;
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
			DBCon.checkCon();
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
			DBCon.checkCon();
		}
		return null;
	}
	
	/**
	 * supposed to fetch all files from fileserver which are like name
	 * 
	 * @param isin
	 * @return
	 */
	public static FileModelContainer getFiles(String isin, int id) {
		try {
			FileModelContainer fm = new FileModelContainer();
			PreparedStatement cstmt = DBCon.con.prepareStatement("select * from vv_fileserver where fs_isin=? and fs_fk_user=?;");
			cstmt.setString(1, isin);
			cstmt.setInt(2, id);
			ResultSet rs = cstmt.executeQuery();
			fm.fill(rs);
			return fm;
		} catch (SQLException e) {
			e.printStackTrace();
			DBCon.checkCon();
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
			DBCon.checkCon();
		}
		return null;
	}

}
