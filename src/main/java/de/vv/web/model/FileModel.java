package de.vv.web.model;

public class FileModel {
	public String establishment;
	public String comment;
	public String fileName;
	public String timeStamp;

	public FileModel() {
	}

	public FileModel(String e, String c, String f, String t) {
		establishment = e.trim();
		comment = c.trim();
		fileName = f.trim();
		timeStamp = t.trim();
	}

}
