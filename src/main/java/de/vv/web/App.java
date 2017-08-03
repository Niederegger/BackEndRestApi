package de.vv.web;

import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import de.vv.web.config.Config;
import de.vv.web.db.DBCon;
import de.vv.web.functions.BasicFunctions;
import de.vv.web.functions.MimeHandling;
import de.vv.web.functions.Scheduler;

@SpringBootApplication
// this part disables security
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
public class App {

	public static Config config = new Config();
	
	public static void main(String[] args) {
		
		
		if(args.length != 1){																			// Wurde eine Config weitergegebene?
			System.err.println("Please enter serverConfig path");
			return;
		}
		
		if(BasicFunctions.loadConfig(args[0])){										// Ja - Continue
			DBCon.openConnection();																	// Verbindung mit Datenbank erstellen
			MimeHandling.initMappings();														// sets up various different Mimes for possible Files (file-Endings)
			Thread scheduleThread = new Thread(new Scheduler());		// starts Scheduler Thread (fetches regularly all distinct ISIN for Autocomplete)
			scheduleThread.start();	
			
			SpringApplication.run(App.class, args);									// Start der App
		} else 
			return;
	}
	

	@Controller
	public class ServletConfig {
	    @Bean
	    public EmbeddedServletContainerCustomizer containerCustomizer() {
	        return (container -> {
	            container.setPort(config.thisPort);							// Setzen des Ports, welche über Config gesetzt wurde
	        });
	    }
	}

}
