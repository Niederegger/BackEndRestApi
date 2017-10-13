package de.vv.web.model.files;

import java.text.DecimalFormat;

import org.springframework.web.multipart.MultipartFile;

import de.vv.web.functions.BF;
import de.vv.web.functions.FileHandling;

public class FileUploadInformation {
	public String isin;								// Unternehmen Name
	public String comment;						// Kommentar
	public MultipartFile uploadFile;	// File-Upload
	public String name;								// Name der File
	public String dataOrigin;					// Quelle der Datei
	public String dataType;						// Typ der Datei


	public FileUploadInformation() {
	}

	public FileUploadInformation(String isin, String dataOrigin, MultipartFile uf) {
		if (isin == null || dataOrigin == null)
			return;
		this.isin = BF.isinOfWkn(isin.trim());
		this.dataOrigin = "unregistered User";
		this.uploadFile = uf;
		comment = FileHandling.getSize(uf);
		dataType = FileHandling.getType(uf);
		name = dataOrigin.trim() + "." + dataType;
	}

	public boolean valid() {
		if (validStr(isin) && validStr(dataOrigin) && uploadFile != null && !uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename().trim();
			return BF.validUploadType(fileName);
		}
		return false;
	}

	public boolean validStr(String str) {
		return (str != null && str != "");
	}

	public FileData toFileData(String ip, String location, int id) {
		return new FileData(name, location, id, ip, comment, isin, dataOrigin, dataType);
	}

	@Override
	public String toString() {
		return "isin: " + isin + "\ncomment: " + comment + "\name: " + name + "\ndataOrigin: " + dataOrigin + "\ndataType: "
				+ dataType;
	}
}
