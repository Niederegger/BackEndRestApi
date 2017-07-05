package de.vv.web.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IsinDataRow
 * 
 * @author Alexey Gasevic
 *
 */
public class IdRow {
	public String mv_fieldname;
	public String mv_StringValue;
	public String mv_as_of_date;
	public String mv_timestamp;
	public String mv_source_id;
	

	public static String qmv_fieldname = "mv_fieldname";
	public static String qmv_StringValue = "mv_StringValue";
	public static String qmv_as_of_date = "mv_as_of_date";
	public static String qmv_timestamp = "mv_timestamp";
	public static String qmv_source_id = "mv_source_id";
	
	public IdRow(String fieldname, String stringValue, String asOfDate, String timestamp, String sourceId) {
		mv_fieldname = fieldname;
		mv_StringValue = stringValue;
		mv_as_of_date = asOfDate;
		mv_timestamp = timestamp;
		mv_source_id = sourceId;
	}

	public IdRow() {
	}

	public static IdRow parse(ResultSet rs) throws SQLException {
		String[] init = new String[5];
		int i = 0;
		init[i++] = rs.getString(qmv_fieldname);
		init[i++] = rs.getString(qmv_StringValue);
		init[i++] = rs.getString(qmv_as_of_date);
		init[i++] = rs.getString(qmv_timestamp);
		init[i++] = rs.getString(qmv_source_id);
		for(i = 0; i < init.length; i++) {
			if(init[i] != null) init[i] = init[i].trim();
		}
		i = 0;
		return new IdRow(init[i++],init[i++],init[i++],init[i++],init[i++]);
	}
}
