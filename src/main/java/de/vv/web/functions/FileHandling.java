package de.vv.web.functions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import de.vv.web.AjaxDemoApplication;
import de.vv.web.model.FileUploadInformation;

public class FileHandling {

	/**
	 * stores file to disk
	 * 
	 * @param uploadfile
	 *            - uploaded file
	 * @param name
	 *            - filename
	 * @param dir
	 *            - directory for saving
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String storeFile(FileUploadInformation uploadFile, String subfolder) {
		File dir = new File(AjaxDemoApplication.config.fileLocation);
		if (!dir.exists())
			dir.mkdirs();
		File serverFile = new File(dir.getAbsolutePath() + File.separator + subfolder + File.separator + uploadFile.name);
		if(!serverFile.getParentFile().exists()) serverFile.getParentFile().mkdirs();
		byte[] buffer = new byte[1024];
		InputStream reader;
		try {
			reader = new BufferedInputStream(uploadFile.uploadfile.getInputStream());
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

}
