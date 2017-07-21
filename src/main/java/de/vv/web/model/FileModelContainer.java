package de.vv.web.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileModelContainer {
	public List<FileModel> data = new ArrayList<FileModel>();

	public String[] queryCoulmns = { "fs_filename", "fs_establishment", "fs_timestamp", "fs_comment" };

	public String queryString = "select fs_filename, fs_establishment, fs_timestamp, fs_comment From  vv_fileserver t1 where fs_timestamp = (select max(fs_timestamp) from vv_fileserver where t1.fs_location =vv_fileserver.fs_location) order by fs_timestamp desc;";

	public void fill(ResultSet rs) {
		try {
			while (rs.next()) { // ecft
				data.add(new FileModel(rs.getString(queryCoulmns[1]), // fs_establishment
																		// e
						rs.getString(queryCoulmns[3]), // fs_comment c
						rs.getString(queryCoulmns[0]), // fs_filename f
						rs.getString(queryCoulmns[2]) // fs_timestamp t
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		sort();
	}

	public void sort() {
		for (int i = 0; i < data.size(); i++) {
			for (int j = i + 1; j < data.size(); j++) {
				// camparer to sort establishment by names
				if (data.get(i).establishment.compareTo(data.get(j).establishment) > 0) {
					FileModel tmp = data.get(i);
					data.set(i, data.get(j));
					data.set(j, tmp);
				}
			}
		}
	}

}
