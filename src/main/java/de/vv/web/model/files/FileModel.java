package de.vv.web.model.files;

public class FileModel {
	public String isin;
	public String comment;
	public String fileName;
	public String timeStamp;
	public String dataOrigin;
	public String dataType;

	public FileModel() {
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
