package de.vv.web.functions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.vv.web.App;
import de.vv.web.model.files.FileUploadInformation;

public class FileHandling {

	/**
	 * stores file to disk
	 * 
	 * @param uploadFile
	 *          - uploaded file
	 * @param name
	 *          - filename
	 * @param dir
	 *          - directory for saving
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String storeFile(FileUploadInformation uploadFile, String subfolder) {
		// initial Setup
		
		File dir = new File(App.config.fileLocation);																																				// getting FileServerLocation
		if (!dir.exists())																																																	// checks whether this directory exists
			dir.mkdirs();																																																			// if not create it
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());																				// gets current TimeStamp
		System.out.println("2"+uploadFile.name);
		File serverFile = new File(																																													// build fullPath + name of this File
				dir.getAbsolutePath() + File.separator + subfolder + File.separator + timeStamp + uploadFile.name);	
		if (!serverFile.getParentFile().exists())																																						// check if path exists
			serverFile.getParentFile().mkdirs();																																							// create if doesn't
		
		// start writing
		
		byte[] buffer = new byte[1024];
		InputStream reader;
		try {
			reader = new BufferedInputStream(uploadFile.uploadFile.getInputStream());
			OutputStream writer = new FileOutputStream(serverFile);

			int read;
			while ((read = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, read);
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return serverFile.getAbsolutePath();
	}

	/**
	 * deletes File from FileServer
	 * 
	 * @param path
	 *          kopmlete path to File
	 * @return
	 */
	public static boolean deleteFile(String path) {
		try {
			return new File(path).delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
