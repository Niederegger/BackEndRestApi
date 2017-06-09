package de.vv.web;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import de.vv.web.config.Config;
import de.vv.web.db.DBCon;
import de.vv.web.functions.MimeHandling;
import de.vv.web.model.UserRoles;

@SpringBootApplication
public class AjaxDemoApplication {

	public static Config config = new Config();
	
	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("Please enter serverConfig path");
			return;
		}
		if(loadConfig(args[0])){
			DBCon.openConnection();
			MimeHandling.initMappings();
			UserRoles.loadRoles();
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
	
//	@Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }
}
