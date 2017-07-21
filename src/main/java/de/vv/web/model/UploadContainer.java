package de.vv.web.model;

import java.util.ArrayList;
import java.util.List;

public class UploadContainer {
	public String query = "INSERT INTO vv_mastervalues_upload"
			+ "(MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
			+ "(?,?,?,?,?,?,?);";

	public String isin;
	public String mic;
	public String source;
	public String origin;
	public String comment;
	public String urlSource;
	
	
	public List<String> fieldName = new ArrayList<String>();
	public List<String> values = new ArrayList<String>();
	

	public boolean validate() {
		if (mic == null)
			mic = "";
		return (isin != null && isin.length() == 12 && mic.length() <= 4 && origin != null);
	}
	
	public void setData(String source, String origin, String comment, String mic){
		this.source = source;
		this.origin = origin;
		this.urlSource = origin;
		this.comment = comment;
		this.mic = mic;
	}
	
	public void setData(String source, String origin, String urlSource, String comment, String mic){
		this.source = source;
		this.origin = origin;
		this.urlSource = urlSource;
		this.comment = comment;
		this.mic = mic;
	}
}
