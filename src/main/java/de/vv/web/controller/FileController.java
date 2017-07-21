package de.vv.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

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

import de.vv.web.db.DBCon;
import de.vv.web.functions.*;
import de.vv.web.model.*;

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
	public @ResponseBody String uploadFileHandler(HttpServletRequest request, 
			@RequestParam(value = "establishment") String establishment,
			@RequestParam(value = "comment") String comment,
	        @RequestParam(value = "file") MultipartFile file) {
		FileUploadInformation fui = new FileUploadInformation();
		fui.comment = comment.trim();
		fui.establishment = establishment.trim();
		fui.uploadfile = file;
		System.out.println(fui.toString());
		if (fui.valid()) {
			try { // directory des fileservers

				// store file
				String location = FileHandling.storeFile(fui, "-1".trim());
				if(location == null) return "Error when saving File.";
				// create entry to db
				DBCon.fileUploadEntry(fui.toFileData(request.getRemoteAddr(), location, -1)); // eintrag in die db
				return "Successfully uploaded file => " + fui.name;
			} catch (Exception e) {
				e.printStackTrace();
				return "Failed to upload => " + e.getMessage();
			}
		} else {
			return "Failed to upload because upload was invalid.";
		}
	}
	

	@RequestMapping(value = "/fetchFileData", method = RequestMethod.GET)
	public @ResponseBody FileModelContainer fetchFiles() {
		return DBCon.getAllFiles();
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> downloadFile(@RequestParam("fn") String fn, @RequestParam("ts") String ts) {
		System.out.println(fn);
		HttpHeaders headers = new HttpHeaders();
		System.out.println(ts);
		headers.setContentType(MediaType.parseMediaType(MimeHandling.GetMimeType(fn)));
		headers.add("content-disposition", "attachment; filename=" + fn);
		String[] arr = ts.split("_");
		String normalizedTS = arr[0] + " " + arr[1];
		System.out.println(normalizedTS);
		String location = DBCon.getFileLocation(fn, normalizedTS);
		File file = FileUtils.getFile(location);

		FileSystemResource fileSystemResource = new FileSystemResource(file);

		return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
	}

}
