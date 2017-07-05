package de.vv.web;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

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
	
//	@Configuration
//	public class MvcConfig extends WebMvcConfigurerAdapter {
//
//	    @Override
//	    public void addViewControllers(ViewControllerRegistry registry) {
//	        registry.addViewController("/**/*.html").setViewName("");
//	    }
//
//	}
	
//	/**
//	 * This application is secured at both the URL level for some parts, and the method level for other parts. The URL
//	 * security is shown inside this code, while method-level annotations are enabled at by
//	 * {@link EnableGlobalMethodSecurity}.
//	 *
//	 * @author Greg Turnquist
//	 * @author Oliver Gierke
//	 */
//	@Configuration
//	@EnableGlobalMethodSecurity(prePostEnabled = true)
//	@EnableWebSecurity
//	static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//		/**
//		 * This section defines the user accounts which can be used for authentication as well as the roles each user has.
//		 * 
//		 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
//		 */
//		@Override
//		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//			auth.inMemoryAuthentication().//
//					withUser("user").password("wassabi").roles("USER").and().//
//					withUser("admin").password("coolio").roles("USER", "ADMIN");
//		}
//
//		/**
//		 * This section defines the security policy for the app.
//		 * <p>
//		 * <ul>
//		 * <li>BASIC authentication is supported (enough for this REST-based demo).</li>
//		 * <li>/employees is secured using URL security shown below.</li>
//		 * <li>CSRF headers are disabled since we are only testing the REST interface, not a web one.</li>
//		 * </ul>
//		 * NOTE: GET is not shown which defaults to permitted.
//		 *
//		 * @param http
//		 * @throws Exception
//		 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
//		 */
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//
//			http.httpBasic().and().authorizeRequests().//
//					antMatchers("/**/*.html").permitAll().anyRequest().authenticated();
//		}
//	}
//	
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
