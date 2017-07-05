package de.vv.web.controller;

import java.io.File;
import java.util.List;

import javax.activation.MimeType;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.vv.web.AjaxDemoApplication;
import de.vv.web.db.DBCon;
import de.vv.web.functions.FileHandling;
import de.vv.web.functions.MimeHandling;
import de.vv.web.model.FileData;

@Controller
@RequestMapping("/api/file")
public class FileController {

	/**
	 * Controller handling file uploads
	 * 
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("uploadfile") MultipartFile uploadfile) {
		String name = uploadfile.getOriginalFilename(); // name der datei
		if (!uploadfile.isEmpty()) {
			try { // directory des fileservers
				File dir = new File(AjaxDemoApplication.config.fileLocation);
				// informationen fuer db
				FileData fh = new FileData(name, (dir.getAbsolutePath() + File.separator + name), -1);
				// -------------------------------------------------------------
				// todo: validate whether this file is allowed to be saved!
				// -------------------------------------------------------------

				// store file
				FileHandling.storeFile(uploadfile, name, dir); // speichern der
																// datei
				// create entry to db
				DBCon.fileUploadEntry(fh); // eintrag in die db
				return "You successfully uploaded file => " + name;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	@RequestMapping(value = "/fetchFile", method = RequestMethod.GET)
	public @ResponseBody List<String> fetchFiles(@RequestParam("name") String name) {
		return DBCon.getAllFiles(name == "*" ? null : name);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> downloadFile(@RequestParam("name") String name) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MimeHandling.GetMimeType(name)));
		headers.add("content-disposition", "attachment; filename=" + name);
		String location = DBCon.getFileLocation(name);
		File file = FileUtils.getFile(location);

		FileSystemResource fileSystemResource = new FileSystemResource(file);

		return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
	}

}
