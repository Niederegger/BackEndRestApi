package de.vv.web.model.files;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.vv.web.db.DBC_User;
import de.vv.web.model.user.UserModel;

public class FileModel {
	public String isin;
	public String comment;
	public String fileName;
	public String timeStamp;
	public String dataOrigin;
	public String dataType;
	public String source;
	public String url;
	public boolean extern;

	public FileModel() {
	}

	public FileModel(ResultSet rs) {
		init(rs);
	}

	public void init(ResultSet rs) {
		try {
			int fsfkuser = rs.getInt("fs_fk_user");
			if (fsfkuser == -2)	extern = true;
			isin = rs.getString("fs_isin");
			if(isin!=null)isin=isin.trim();
			comment = rs.getString("fs_comment");
			if(comment!=null)comment=comment.trim();
			fileName = rs.getString("fs_filename");
			if(fileName!=null)fileName=fileName.trim();
			timeStamp = rs.getString("fs_timestamp");
			if(timeStamp!=null)timeStamp=timeStamp.trim();
			dataType = rs.getString("fs_data_type");
			if(dataType!=null)dataType=dataType.trim();
			dataOrigin = rs.getString("fs_data_origin");
			if(dataOrigin!=null)dataOrigin=dataOrigin.trim();
			source = rs.getString("fs_ip");
			if(source!=null)source=source.trim();
			if (extern)	url = rs.getString("fs_location");
			else if(fsfkuser == -1) url = "unregistered User";
			else {
				UserModel um = DBC_User.findByID(fsfkuser);
				if(um != null) url = um.username;
				else  url = "unknown source";
			}
			if(extern && url!=null)url=url.trim();
			if(!extern)source="";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileModel(String i, String c, String f, String t, String dor, String dt) {
		isin = i.trim();
		comment = c.trim();
		fileName = f.trim();
		timeStamp = t.trim();
		dataOrigin = dor.trim();
		dataType = dt.trim();
	}

}
