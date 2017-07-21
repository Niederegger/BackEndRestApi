package de.vv.web.model;

import org.springframework.web.multipart.MultipartFile;

import de.vv.web.functions.BasicFunctions;

public class FileUploadInformation {
	public String establishment;		// Unternehmen Name
	public String comment;				// Kommentar
	public MultipartFile uploadfile;	// File-Upload
	public String name;					// Name der File
	
	public boolean valid(){
		if(establishment != null && establishment != "" && uploadfile != null && !uploadfile.isEmpty()){
			name = uploadfile.getOriginalFilename().trim();
			return BasicFunctions.validUploadType(name);
		}
		return false;
	}
	
	public FileData toFileData(String ip, String location, int id){
		return new FileData(name, location, id, ip, comment, establishment);
	}
	
	@Override
	public String toString() {
		return "establishment: " + establishment
				+"\ncomment: " + comment
				+"\name: " + name;
	}
}
