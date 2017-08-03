package de.vv.web.model.UpdateIsinHistory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryIsinUpdatesContainer {
	public List<HistoryIsinUpdatesModel> container = new ArrayList<HistoryIsinUpdatesModel>();
	
	public HistoryIsinUpdatesContainer(ResultSet rs) throws SQLException{
		while(rs.next()){
			container.add(new HistoryIsinUpdatesModel(rs));
		}
	}
}
