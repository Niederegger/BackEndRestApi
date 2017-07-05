package de.vv.web.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BasicFunctions {
	public static BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
	
	
	public static void init() {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
	}

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Pattern emailPattern;
	private static Matcher matcher;

	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validateEmail(final String hex) {
		matcher = emailPattern.matcher(hex);
		return matcher.matches();
	}

	public static String encodePassword(String password){
//		return bcpe.encode(password);
		return password;
	}
	
}
