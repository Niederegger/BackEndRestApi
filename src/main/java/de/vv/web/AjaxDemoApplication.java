package de.vv.web;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import de.vv.web.config.Config;
import de.vv.web.db.DBCon;
import de.vv.web.functions.BasicFunctions;
import de.vv.web.functions.MimeHandling;
import de.vv.web.functions.Scheduler;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
public class AjaxDemoApplication {

	public static Config config = new Config();
	
	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("Please enter serverConfig path");
			return;
		}
		if(loadConfig(args[0])){
			BasicFunctions.init();
			DBCon.openConnection();
			MimeHandling.initMappings();
			Thread scheduleThread = new Thread(new Scheduler());
			scheduleThread.start();
			SpringApplication.run(AjaxDemoApplication.class, args);
		} else 
			return;
		
	}
	
	public static boolean loadConfig(String file) {
		Gson gson = new Gson();
		try { // die Config wird als Json object aus dem Dateipfad geladen und
				// in ein Config Object konvertiert
			config = gson.fromJson(new FileReader(file), Config.class);
			return true;
		} catch (JsonSyntaxException e) {
			System.err.println();
			System.err.println("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			System.err.println("JsonIOException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		return false;
	}
	
	@Controller
	public class ServletConfig {
	    @Bean
	    public EmbeddedServletContainerCustomizer containerCustomizer() {
	        return (container -> {
	            container.setPort(config.thisPort);
	        });
	    }
	}
}
