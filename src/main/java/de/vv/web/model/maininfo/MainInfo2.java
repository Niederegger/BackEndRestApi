package de.vv.web.model.maininfo;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.vv.web.model.StringInt;

public class MainInfo2 {
	
	//--------------------------------------------------------------------------------------------------------------------
	// DB_Result_Column_Names_Definition: ~
	//--------------------------------------------------------------------------------------------------------------------
	
	private String DB_sequence_num = "SEQUENCE_NUM";
	private String DB_level1 = "fieldname";
	private String DB_stringValue = "STRINGVALUE";
	private String DB_data_origin = "DATA_ORIGIN";
	private String DB_urlsource = "URLSOURCE";
	private String DB_source_num = "SOURCE_NUM";
	private String DB_timestamp = "record_date";
	private String DB_sourceId = "Source_Id";

	
	//--------------------------------------------------------------------------------------------------------------------
	// ordinary variables:
	//--------------------------------------------------------------------------------------------------------------------
	
	public int sequence_num;														// SequenceNumber (look at Filed_definitions)
	public String level1;															// Level 1 Header
	public String level2;															// Level 2 Header
	public String stringValue;														// StringValue of this Item
	public String data_origin;														// Origin of this Item
	public String urlsource;														// Url-Source of this Item
	public int source_num;															// Priority of this Source
	public String timestamp;														// Timestamp of Entry
	public String sourceId;															// Id of Source
	
	//--------------------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------------------
	
	public MainInfo2() {}
	
	public MainInfo2(String fn, int sqn) {
		level1 = fn; sequence_num = sqn;
		normalize();
	}

	/**
	 * used to generate an DataObject directly fetching data from ResultSet
	 * 
	 * @param rs
	 */
	public MainInfo2(ResultSet rs) {
		try {
			sequence_num = rs.getInt(DB_sequence_num);
			source_num = rs.getInt(DB_source_num);
			// Strings
			level1 = rs.getString(DB_level1);
			if (level1 != null)level1 = level1.trim();
			stringValue = rs.getString(DB_stringValue);
			if (stringValue != null)stringValue = stringValue.trim();
			data_origin = rs.getString(DB_data_origin);
			if (data_origin != null)data_origin = data_origin.trim();
			urlsource = rs.getString(DB_urlsource);
			if (urlsource != null)urlsource = urlsource.trim();
			sourceId = rs.getString(DB_sourceId);
			if (sourceId != null)sourceId = sourceId.trim();
			timestamp = rs.getString(DB_timestamp);
			if (timestamp != null)timestamp = timestamp.trim();
			if(sourceId.equals("User")) urlsource = "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	//--------------------------------------------------------------------------------------------------------------------
	// Functional
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * uses dot to seperate lvl1 from lvl 2 header
	 */
	public void normalize() {
		if (level1 != null && level1.contains(".")) {
			String[] tmp = level1.split("\\.");
			level1 = tmp[0];
			level2 = tmp[1];
		}
	}

	/**
	 * returns whether the sequence number are identical
	 * 
	 * @param mi2
	 *          MainInfo2
	 * @return boolean
	 */
	public boolean sameSeq(MainInfo2 mi2) {
		return mi2.sequence_num == sequence_num;
	}

	/**
	 * returns whether sequence and source Nums are identical
	 * 
	 * @param sqn
	 * @param srcn
	 * @return
	 */
	public boolean sameSqSn(int sqn, int srcn) {
		return sequence_num == sqn && source_num == srcn;
	}

	/**
	 * returns whether this Items priority is higher than the other
	 * 
	 * @param mi2
	 * @return
	 */
	public boolean higherPrior(MainInfo2 mi2) {
		return mi2.source_num < mi2.source_num;
	}

	/**
	 * 
	 * @param q
	 *          String represents Reference Text of Source and Int the sequence Num
	 * @return
	 */
	public QuellenEntry toQuellenEntry(StringInt[] q) {
		return new QuellenEntry(level2, stringValue, sequence_num, source_num, urlsource, data_origin, sourceId, timestamp, q);
	}

	/**
	 * converts this object into a QuellenEntry
	 * 
	 * @return
	 */
	public QuellenEntry toQuellenEntry() {
		return new QuellenEntry(level2, stringValue, sequence_num, source_num, urlsource, data_origin, sourceId, timestamp, null);
	}

	//--------------------------------------------------------------------------------------------------------------------
	// Overrides
	//--------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(DB_sequence_num);
		sb.append(": ");
		sb.append(sequence_num);
		sb.append(",\n");
		sb.append(DB_level1);
		sb.append(": ");
		sb.append(level1);
		sb.append(",\n");
		sb.append(level2);
		sb.append(",\n");
		sb.append(DB_stringValue);
		sb.append(": ");
		sb.append(stringValue);
		sb.append(",\n");
		sb.append(DB_data_origin);
		sb.append(": ");
		sb.append(data_origin);
		sb.append(",\n");
		sb.append(DB_urlsource);
		sb.append(": ");
		sb.append(urlsource);
		sb.append(",\n");
		sb.append(DB_source_num);
		sb.append(": ");
		sb.append(source_num);
		return sb.toString();
	}

}
