package de.vv.web.model.files;

import java.text.DecimalFormat;

import org.springframework.web.multipart.MultipartFile;

import de.vv.web.functions.BasicFunctions;

public class FileUploadInformation {
	public String isin;								// Unternehmen Name
	public String comment;						// Kommentar
	public MultipartFile uploadFile;	// File-Upload
	public String name;								// Name der File
	public String dataOrigin;					// Quelle der Datei
	public String dataType;						// Typ der Datei

	DecimalFormat df = new DecimalFormat("0.00");

	float sizeKb = 1024.0f;
	float sizeMb = sizeKb * sizeKb;
	float sizeGb = sizeMb * sizeKb;
	float sizeTerra = sizeGb * sizeKb;

	public FileUploadInformation() {
	}

	public FileUploadInformation(String isin, String dataOrigin, MultipartFile uf) {
		if (isin == null || dataOrigin == null)
			return;
		this.isin = BasicFunctions.isinOfWkn(isin.trim());
		this.dataOrigin = "unregistered User";
		this.uploadFile = uf;
		long size = uf.getSize();
		if (size < sizeMb)
			comment = df.format(size / sizeKb) + " Kb";
		else if (size < sizeGb)
			comment = df.format(size / sizeMb) + " Mb";
		else comment = df.format(size / sizeGb) + " Gb";
		String[] dtar = uf.getOriginalFilename().split("\\.");
		dataType = dtar[dtar.length-1];
		name = dataOrigin.trim() + "." + dataType;
	}

	public boolean valid() {
		if (validStr(isin) && validStr(dataOrigin) && uploadFile != null && !uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename().trim();
			return BasicFunctions.validUploadType(fileName);
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
