package de.vv.web.model.UpdateIsinHistory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryIsinUpdatesModel {
	public String isin;
	public String source;
	public String timestamp;

	public String dbIsin = "MV_ISIN";
	public String dbSource = "MV_SOURCE_ID";
	public String dbTimestamp = "UPL_TIMESTAMP";
	
	public HistoryIsinUpdatesModel(){}
	
	public HistoryIsinUpdatesModel(ResultSet rs) throws SQLException{
		this.isin = rs.getString(dbIsin);
		this.source = rs.getString(dbSource);
		this.timestamp = rs.getString(dbTimestamp);
	}
	
	public HistoryIsinUpdatesModel(String isin, String source, String timestamp){
		this.isin = isin;
		this.source = source;
		this.timestamp = timestamp;
	}
		
}
