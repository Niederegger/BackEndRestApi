package de.vv.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.vv.web.db.DBC_FileSystem;
import de.vv.web.functions.*;
import de.vv.web.model.Strings2;
import de.vv.web.model.files.FileModelContainer;
import de.vv.web.model.files.FileUploadInformation;

@RestController
@RequestMapping("/api/file")
public class FileController {

	/**
	 * Controller to Upload Files to this Server
	 * 
	 * @param request
	 *          Used to fetch Ip
	 * @param isin
	 *          ISIN belonging to this request
	 * @param dataType
	 *          ("Typ")
	 * @param dataOrigin
	 *          ("Quelle")
	 * @param comment
	 *          ("Kommentar")
	 * @param file
	 *          ("Die Datei an sich")
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(HttpServletRequest request, @RequestParam(value = "isin") String isin,
			@RequestParam(value = "dataOrigin") String dataOrigin,@RequestParam(value = "file") MultipartFile file) {

		FileUploadInformation fui = new FileUploadInformation(isin, dataOrigin, file);
		if (fui.valid()) {
			try {

				System.out.println("1:" + fui.name);
				// store file
				String location = FileHandling.storeFile(fui, fui.isin);
				if (location == null)
					return "Error when saving File.";
				
				// create entry to db
				DBC_FileSystem.fileUploadEntry(fui.toFileData(request.getRemoteAddr(), location, -1)); // eintrag in die db
				return "Successfully uploaded file => " + fui.name;

			} catch (Exception e) {
				e.printStackTrace();
				return "Failed to upload => " + e.getMessage();
			}
		} else {
			return "Failed to upload because upload was invalid.";
		}
	}

	/**
	 * fetches all saved Informations of Files belonging to this ISIN
	 * 
	 * @param isin
	 *          ISIN
	 * @return FileModelContainer
	 */
	@RequestMapping(value = "/fetchFileData", method = RequestMethod.GET)
	public @ResponseBody FileModelContainer fetchFiles(@RequestParam("isin") String isin) {
		return DBC_FileSystem.getFiles(BasicFunctions.isinOfWkn(isin));
	}

	/**
	 * File Download Controller
	 * 
	 * @param fn
	 *          FileName
	 * @param ts
	 *          TimeStamp
	 * @return DownloadEvent
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> downloadFile(@RequestParam("fn") String fn, @RequestParam("ts") String ts) {
		String normalizedTS = normalizeTimeStamp(ts);
		String location = DBC_FileSystem.getFileLocation(fn, normalizedTS);
		File file = FileUtils.getFile(location);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MimeHandling.GetMimeType(fn)));
		headers.add("content-disposition", "attachment; filename=" + fn);
		
		FileSystemResource fileSystemResource = new FileSystemResource(file);

		return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
	}

	/**
	 * Delete File Controller: deleting File from server and from Database
	 * 
	 * @param val
	 *          carries Timestamp and Filename
	 * @return Result-Message
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String removeFile(@RequestBody Strings2 val) {
		if (val.str2 == null || val.str2 == "") {
			return "Error, wrong input";
		}
		String location = DBC_FileSystem.getFileLocation(val.str1, normalizeTimeStamp(val.str2));
		if (location != null) {
			if (FileHandling.deleteFile(location)) {		// deleting data from File-Server
				if (DBC_FileSystem.removeFileEntry(location)) {		// deleting Entry from DB
					return "Delete success.";
				} else {
					return "Couldn't remove Entry.";
				}
			} else {
				return "Couldn't remove File.";
			}
		} else {
			return "Already deleted.";
		}
	}

	/**
	 * Since a special form of TimeStamp is used (replaces whitespace with '_')
	 * this TimeStamp has to be parsed back to normal to be usefull again
	 * 
	 * @param ts
	 *          TimeStamp
	 * @return normalized TimeStamp
	 */
	String normalizeTimeStamp(String ts) {
		String[] arr = ts.split("_");
		return arr[0] + " " + arr[1];
	}

}
