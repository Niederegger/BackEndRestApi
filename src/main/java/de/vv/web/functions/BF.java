package de.vv.web.functions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import de.vv.web.App;
import de.vv.web.config.Config;
import de.vv.web.db.DBC_WP;

public class BF {

	//--------------------------------------------------------------------------------------------------------------------
	// Stringify
	//--------------------------------------------------------------------------------------------------------------------

	public static String trimming(String s){
		if(s!=null) return s.trim();
		else return s;
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// static Variables
	//--------------------------------------------------------------------------------------------------------------------

	public static BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();																								// currently unused
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"																	// regex-Pattern to validate E-Mails
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	
	private static Pattern emailPattern;																																									
	private static Matcher matcher;											
	public static final String[] uploadExtension = { "xlsx", "xlsm", "xlsb", "xltx", "xltm", "xls", "xlt", "xml", "xlam",	// allowed File-Extensions
			"xla", "xlw", "xlr", "csv", "pdf", };

	//--------------------------------------------------------------------------------------------------------------------
	// config
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * loads Config from File
	 * @param fullPath full File Path
	 * @return true if successful
	 */
	public static boolean loadConfig(String fullPath) {
		Gson gson = new Gson();
		try {
			App.config = gson.fromJson(new FileReader(fullPath), Config.class);							// setup static Config
			return true;
		} catch (JsonSyntaxException e) {
			System.err.println("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			System.err.println("JsonIOException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		emailPattern = Pattern.compile(EMAIL_PATTERN);																		// compiles Email-Pattern
		return false;
	}
	
	public static Object parseJson(String str, Object o) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(str, o.getClass());
		} catch (JsonSyntaxException e) {
			System.err.println("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			System.err.println("JsonIOException: " + e.getMessage());
		} 
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// ISIN Check
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * checks wether this WKN belongs to an ISIN, and if returns that ISIN
	 * 
	 * @param wkn
	 *          WKN
	 * @return ISIN or null
	 */
	public static String isinOfWkn(String wkn) {
		wkn = wkn.trim();
		if (wkn != null && wkn.length() == 6) {		// is wkn 6 characters long?
			return DBC_WP.getIsinOfWkn(wkn);				// fetching first ISIN found linked to this wkn
		} else if (wkn.length() == 12) {					// is wkn 12 characters long? -> it might already be an ISIN
			return wkn;
		}
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// Validator
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *          hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validateEmail(final String hex) {
		matcher = emailPattern.matcher(hex);
		return matcher.matches();
	}

	/**
	 * checks whether this File has one acceptable ending
	 * 
	 * @param file
	 *          FileName
	 * @return true / false
	 */
	public static boolean validUploadType(String file) {
		String[] tmp = file.split("\\.");
		String ending = tmp[tmp.length - 1].toLowerCase();
		for (String s : uploadExtension) {
			if (ending.equals(s))
				return true;
		}
		return false;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// Account
	//--------------------------------------------------------------------------------------------------------------------

	public static String encodePassword(String password) {
		//	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return bcpe.encode(password);
//		return password;
	}
	
	public static String decodePassword(String password) {
		//	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return bcpe.encode(password);
//		return password;
	}

}
