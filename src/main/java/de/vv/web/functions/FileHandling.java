package de.vv.web.functions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

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
	public static void storeFile(MultipartFile uploadfile, String name, File dir)
			throws IOException, FileNotFoundException {
		if (!dir.exists())
			dir.mkdirs();
		File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
		byte[] buffer = new byte[1024];
		InputStream reader = new BufferedInputStream(uploadfile.getInputStream());
		OutputStream writer = new FileOutputStream(serverFile);

		int read;
		while ((read = reader.read(buffer)) > 0) {
			writer.write(buffer, 0, read);
		}
		reader.close();
		writer.close();
	}

}
