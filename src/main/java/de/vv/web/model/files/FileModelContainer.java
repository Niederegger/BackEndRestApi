package de.vv.web.model.files;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileModelContainer {
	public List<FileModel> intern = new ArrayList<FileModel>();
	public List<FileModel> extern = new ArrayList<FileModel>();
	
	
	
//	public String[] queryCoulmns = { "fs_filename", "fs_isin", "fs_timestamp", "fs_comment", "fs_data_origin", "fs_data_type" };

	public void fill(ResultSet rs) {
		try {
			FileModel fm;
			
			while (rs.next()) { // ecftdordt
				fm = new FileModel(rs);
				
				if(fm.extern) extern.add(fm);
				else intern.add(fm);
				
//				extern.add(new FileModel(
//						rs.getString(queryCoulmns[1]), // fs_isin
//						rs.getString(queryCoulmns[3]), // fs_comment   c
//						rs.getString(queryCoulmns[0]), // fs_filename  f
//						rs.getString(queryCoulmns[2]), // fs_timestamp t
//						rs.getString(queryCoulmns[4]), // fs_filename  dor
//						rs.getString(queryCoulmns[5])  // fs_timestamp dt
//						
//				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sort();
	}

	public void sort() {
		for (int i = 0; i < extern.size(); i++) {
			for (int j = i + 1; j < extern.size(); j++) {
				// camparer to sort establishment by names
				if (extern.get(j).timeStamp.compareTo(extern.get(i).timeStamp) > 0) {
					FileModel tmp = extern.get(i);
					extern.set(i, extern.get(j));
					extern.set(j, tmp);
				}
			}
		}
		for (int i = 0; i < intern.size(); i++) {
			for (int j = i + 1; j < intern.size(); j++) {
				// camparer to sort establishment by names
				if (intern.get(j).timeStamp.compareTo(intern.get(i).timeStamp) > 0) {
					FileModel tmp = intern.get(i);
					intern.set(i, intern.get(j));
					intern.set(j, tmp);
				}
			}
		}
	}

}
