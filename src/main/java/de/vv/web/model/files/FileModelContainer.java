package de.vv.web.model.files;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileModelContainer {
	public List<FileModel> data = new ArrayList<FileModel>();

	public String[] queryCoulmns = { "fs_filename", "fs_isin", "fs_timestamp", "fs_comment", "fs_data_origin", "fs_data_type" };

	public void fill(ResultSet rs) {
		try {
			while (rs.next()) { // ecftdordt
				data.add(new FileModel(
						rs.getString(queryCoulmns[1]), // fs_isin
						rs.getString(queryCoulmns[3]), // fs_comment   c
						rs.getString(queryCoulmns[0]), // fs_filename  f
						rs.getString(queryCoulmns[2]), // fs_timestamp t
						rs.getString(queryCoulmns[4]), // fs_filename  dor
						rs.getString(queryCoulmns[5])  // fs_timestamp dt
						
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sort();
	}

	public void sort() {
		for (int i = 0; i < data.size(); i++) {
			for (int j = i + 1; j < data.size(); j++) {
				// camparer to sort establishment by names
				if (data.get(i).timeStamp.compareTo(data.get(j).timeStamp) > 0) {
					FileModel tmp = data.get(i);
					data.set(i, data.get(j));
					data.set(j, tmp);
				}
			}
		}
	}

}
