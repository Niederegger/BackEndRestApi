package de.vv.web.model.files;

import org.springframework.web.multipart.MultipartFile;

import de.vv.web.functions.BasicFunctions;

public class FileUploadInformation {
	public String isin;								// Unternehmen Name
	public String comment;						// Kommentar
	public MultipartFile uploadFile;	// File-Upload
	public String name;								// Name der File
	public String dataOrigin;					// Quelle der Datei
	public String dataType;						// Typ der Datei

	public FileUploadInformation(){}
	
	public FileUploadInformation(String isin, String dataOrigin, String dataType, String comment, MultipartFile uf){
		if(isin == null || dataOrigin == null || dataType == null) return;
		this.isin = BasicFunctions.isinOfWkn(isin.trim());
		this.dataOrigin = dataOrigin.trim();
		this.dataType = dataType.trim();
		if(comment != null) this.comment= comment.trim();
		this.uploadFile = uf;
	}
	
	public boolean valid() {
		if (validStr(isin) && validStr(dataOrigin) && validStr(dataType) && uploadFile != null && !uploadFile.isEmpty()) {
			name = uploadFile.getOriginalFilename().trim();
			return BasicFunctions.validUploadType(name);
		}
		return false;
	}

	public boolean validStr(String str){
		return (str != null && str != "");
	}
	
	public FileData toFileData(String ip, String location, int id) {
		return new FileData(name, location, id, ip, comment, isin, dataOrigin, dataType);
	}

	@Override
	public String toString() {
		return "isin: " + isin
				+"\ncomment: " + comment
				+"\name: " + name
				+"\ndataOrigin: " + dataOrigin
				+"\ndataType: " + dataType;
	}
}
