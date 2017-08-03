package de.vv.web.model.ContainerForOldThoughts;

/**
 * Model Class of MasterValues
 */
public class MasterValue {
	public String MV_SOURCE_ID;
	public String MV_ISIN;
	public String MV_AS_OF_DATE;
	public String MV_FIELDNAME;
	public String MV_TIMESTAMP;
	public String MV_STRINGVALUE;
	public String MV_DATA_ORIGIN;
	public String MV_URLSOURCE;
	public String MV_COMMENT;
	public String MV_MIC;
	public String MV_UPLOAD_ID;

	public MasterValue() {
	}

	public MasterValue(String MV_SOURCE_ID, String MV_ISIN, String MV_AS_OF_DATE, String MV_FIELDNAME,
			String MV_TIMESTAMP, String MV_STRINGVALUE, String MV_DATA_ORIGIN, String MV_URLSOURCE, String MV_COMMENT,
			String MV_MIC, String MV_UPLOAD_ID) {
		this.MV_SOURCE_ID = MV_SOURCE_ID;
		this.MV_ISIN = MV_ISIN;
		this.MV_AS_OF_DATE = MV_AS_OF_DATE;
		this.MV_FIELDNAME = MV_FIELDNAME;
		this.MV_TIMESTAMP = MV_TIMESTAMP;
		this.MV_STRINGVALUE = MV_STRINGVALUE;
		this.MV_DATA_ORIGIN = MV_DATA_ORIGIN;
		this.MV_URLSOURCE = MV_URLSOURCE;
		this.MV_COMMENT = MV_COMMENT;
		this.MV_MIC = MV_MIC;
		this.MV_UPLOAD_ID = MV_UPLOAD_ID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MV_SOURCE_ID: ");
		sb.append(MV_SOURCE_ID);
		sb.append("\nMV_AS_OF_DATE: ");
		sb.append(MV_AS_OF_DATE);
		sb.append("\nMV_FIELDNAME: ");
		sb.append(MV_FIELDNAME);
		sb.append("\nMV_TIMESTAMP: ");
		sb.append(MV_TIMESTAMP);
		sb.append("\nMV_STRINGVALUE: ");
		sb.append(MV_STRINGVALUE);
		sb.append("\nMV_DATA_ORIGIN: ");
		sb.append(MV_DATA_ORIGIN);
		sb.append("\nMV_URLSOURCE: ");
		sb.append(MV_URLSOURCE);
		sb.append("\nMV_COMMENT: ");
		sb.append(MV_COMMENT);
		sb.append("\nMV_MIC: ");
		sb.append(MV_MIC);
		sb.append("\nMV_UPLOAD_ID: ");
		sb.append(MV_UPLOAD_ID);

		return sb.toString();
	}

}
