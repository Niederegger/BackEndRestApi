package de.vv.web.functions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

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
	
	public static String storeFile(MultipartFile file, String name, String subfolder, boolean wts) {
		// initial Setup
		
		File dir = new File(App.config.fileLocation);																																				
		if (!dir.exists())																																																	
			dir.mkdirs();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());	
		String path = dir.getAbsolutePath() + File.separator + subfolder + File.separator;
		if(wts) path += timeStamp;
		path += name;
		File serverFile = new File(path);	
		if (!serverFile.getParentFile().exists())																																						// check if path exists
			serverFile.getParentFile().mkdirs();																																							// create if doesn't
		byte[] buffer = new byte[1024];
		InputStream reader;
		try {
			reader = new BufferedInputStream(file.getInputStream());
			OutputStream writer = new FileOutputStream(serverFile);

			int read;
			while ((read = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, read);
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
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
	
	static DecimalFormat df = new DecimalFormat("0.00");

	static float sizeKb = 1024.0f;
	static float sizeMb = sizeKb * sizeKb;
	static float sizeGb = sizeMb * sizeKb;
	static float sizeTerra = sizeGb * sizeKb;

	public static String getSize(MultipartFile mf){
		long size = mf.getSize();
		String ret = "";
		if (size < sizeMb)
			ret = df.format(size / sizeKb) + " Kb";
		else if (size < sizeGb)
			ret = df.format(size / sizeMb) + " Mb";
		else ret = df.format(size / sizeGb) + " Gb";
		return ret;
	}
	
	public static String getType(MultipartFile mf){
		String[] dtar = mf.getOriginalFilename().split("\\.");
		return dtar[dtar.length-1];
	}
}
