package de.vv.web.functions;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailFunctions {
	public static Session session;

//	public static void test() {
//		sendMail("alexey.gasevic@vv.de", "MailTest",
//				"Dear Mail Crawler," + "<br><br> No spam to my email, please!<br><br><a href='wpwiki.de'>WpWiki</a>");
//	}

	public static void init() {
		final String username = "wpwiki.vvde@gmail.com";
		final String password = "SehrGeheimesPassword!23";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	public static void sendMail(String toMail, String subject, String text) {
		try {
			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			messageBodyPart.setText(text,"UTF-8","html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
