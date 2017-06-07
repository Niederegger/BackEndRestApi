package de.vv.web.model;

/**
 * FileData represents table saved in DB (fileserver)
 * 
 * @author Alexey Gasevic
 */
public class FileData {
	public String filename = "";	// eventuell nicht noetig, da der name bereits in der location steht
	public String location = "";	// der Pfad der datei
	public int userId = -1;			// welcher user hat diese datei hochgeladen

	// names of table comuns
	public final String dbFilename = "[fs_filename]";	
	public final String dbLocation = "[fs_location]";
	public final String dbUserId = "[fs_fk_user]";

	// empty constructor
	public FileData(){}

	public FileData(String filename, String location, int userId) {
		this.filename = filename;
		this.location = location;
		this.userId = userId;
	}

}
