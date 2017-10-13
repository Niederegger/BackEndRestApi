package de.vv.web.controller;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.savedrequest.Enumerator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import de.vv.web.App;
import de.vv.web.db.DBC_User;
import de.vv.web.functions.BF;
import de.vv.web.functions.MailFunctions;
import de.vv.web.model.RegistrationModel;
import de.vv.web.model.user.LoginReturn;
import de.vv.web.model.user.UserModel;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public LoginReturn login(@RequestBody RegistrationModel user, HttpServletRequest request) {
		
		Enumeration<String> ea = request.getHeaderNames();
		
		while(ea.hasMoreElements()){
			String das = ea.nextElement();
			System.out.println(das);
			System.out.println(request.getHeader(das));
			System.out.println("---------------------------------------");
		}
		
		UserModel um = DBC_User.findByEmail(user.email);
		if (um == null)
			return new LoginReturn("Email unbekannt.");
		if (!um.enabled)
			return new LoginReturn("Bitte verifizieren Sie zuerst Ihre Email-Adresse.");
		if (BF.bcpe.matches(user.password, um.password)) {
			return new LoginReturn("Erfolgreiche Anmeldung.", um, um.confirmationToken);
		}
		return new LoginReturn("Falsches Passwort.");
	}

	// Process form input data
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public Object processRegistrationForm(@RequestBody RegistrationModel user, HttpServletRequest request) {

		// Lookup user in database by e-mail
		UserModel userExists = DBC_User.fetchUserByEmailAndName(user.email, user.username);
		if (user.username == null || user.username.length() == 0)
			return "Fehler: Geben Sie einen Nutzernamen ein.";
		else
			user.username = user.username.trim();
		if (user.email == null || user.email.length() == 0)
			return "Fehler: Geben Sie eine Email ein.";
		else
			user.email = user.email.trim();
		if (user.password == null || user.password.length() == 0)
			return "Fehler: Geben Sie ein Passwort ein.";
		else
			user.password = user.password.trim();

		if (userExists != null) {
			String s = "";
			if (userExists.username.equals(user.username))
				s += "Benutzername";
			if (userExists.email.equals(user.email))
				s += (s.length() == 0 ? "" : ", ") + "E-Mail Adresse";

			return "Fehler: Bereits vorhanden: " + s;
		}

		Zxcvbn passwordCheck = new Zxcvbn();

//		Strength strength = passwordCheck.measure(user.password);

//		if (strength.getScore() < 3) {
//			return "Fehler: Ihr Passwort ist zu schwach, versuchen Sie ein stärkeres.";
//		}
		if(user.password.length()<6)return "Fehler: Ihr Passwort ist zu schwach, es muss mindestens 6 Zeichen enthalten.";
		user.password = BF.encodePassword(user.password);// (bCryptPasswordEncoder.encode(user.password));
		System.out.println(user);

		// Generate random 36-character string token for confirmation link

		String confirmationToken = UUID.randomUUID().toString();
		System.out.println(confirmationToken);

		int tries = 0;
		while (DBC_User.findByToken(confirmationToken) != null) {
			if (tries++ > 10000)
				return "Fehler: timeout";
			confirmationToken = UUID.randomUUID().toString();
			System.out.println(confirmationToken);

		}

		userExists = DBC_User.fetchUserByEmailAndName(user.email, user.username);

		DBC_User.saveUser(user.username, user.email, user.password, confirmationToken);

		sendConfirmationMail(user, confirmationToken);

		return "Vielen Dank für Ihre Anmeldung zu WpWiki!"
				+"\n"
				+"\nEs wurde soeben eine E-Mail an die von Ihnen angegebene Adresse versandt."
				+"\nIn dieser E-Mail befindet sich ein Aktivierungs-Link, mit dem Sie Ihre Anmeldung bestätigen können."
				+"\n"
				+"\nBitte klicken Sie auf diesen Link, um die Anmeldung abzuschließen!";
	}

	private void sendConfirmationMail(RegistrationModel user, String confirmationToken) {
		String url = App.config.confirmAdress + BF.encodePassword(confirmationToken)+"&e="+user.email;
		MailFunctions.sendMail(user.email, "Registrierung WpWiki: Email Verifikation",
				"Sie haben sich auf WpWiki registriert."
						+ "<br>Bitte bestätigen Sie ihre Email  indem Sie den folgenden Link aufrufen:"
						+ "<br><a href='" + url + "'>" + url + "<a>");
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm")
	public String processConfirmationForm(@RequestParam(value = "t") String t,@RequestParam(value = "e") String e) {
		UserModel um = DBC_User.findByEmail(e);
		if (um != null && BF.bcpe.matches(um.confirmationToken, t) && !um.enabled) {
			int amt = DBC_User.toggleUserEnabledByToken(um.confirmationToken, true);
			if (amt != 1) {
				System.out.println("Toggle Enabled Error : " + amt);
				MailFunctions.sendMail("alexey.gasevic@vv.de", "toggleEnabledError",
						"<b>Error:UserController</b>" + "<br>" + um.toString() + "<br>" + t + "<br>" + amt);
			}
			return "Ihre Email wurde erfolgreich Verifiziert. <a href=\"/anmelden\">zurück zur Anmeldung</a>";
		}
		return "404 Page not found!";
	}

	@RequestMapping("/test")
	public Object getQuellenElement(@RequestParam(value = "v") String v, @RequestParam(value = "w") String w) {
		switch (v) {
		case "fbe":
			return DBC_User.findByEmail(w);
		case "fbt":
			return DBC_User.findByToken(w);
		case "sum":
			UserModel um = new UserModel();
			um.username = "dieserUser";
			um.password = "qwe";
			um.email = w.split("-")[0];
			um.confirmationToken = w.split("-")[1];

			DBC_User.saveUser("dieserUser", w.split("-")[0], "password", w.split("-")[1]);
			return "ok";

		default:
			break;
		}
		return "---";
	}

	@RequestMapping(value = "/login2", method = RequestMethod.POST, consumes = "application/json")
	public LoginReturn login(@RequestBody UserModel user) {
		System.out.println(user.toString());
		if (user != null && user.email != null) {
			UserModel other = DBC_User.getUserByEmail(user.email);
			if (other == null)
				return new LoginReturn("Anmeldung fehlgeschlagen.");
			if (!other.enabled)
				return new LoginReturn("Bitte verifizieren Sie zuerst Ihre Email-Adresse.");
			if (user.password.equals(other.password)) {
				return new LoginReturn("Successfully logged in", other, "WOw,m8_" + other.email);
			}
		}
		return new LoginReturn("Epic fail.");
	}

	@RequestMapping(value = "/register2", method = RequestMethod.POST, consumes = "application/json")
	public String register(@RequestBody RegistrationModel register) {
		if (register != null && register.check()) {
			register.password = BF.encodePassword(register.password);
			System.out.println(register);
			UserModel user = DBC_User.registerUser(register, "USER");
			System.out.println(user);
			return user != null && user.id > 0 ? "Success" : "Error";
		}
		return "Invalid RegistrationModel";
	}

}
