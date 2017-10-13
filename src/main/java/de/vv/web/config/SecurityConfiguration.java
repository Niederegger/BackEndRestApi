package de.vv.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan("de.vv.web")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// Dis  able CSRF
        // TODO find a way to allow CSRF for everything but API
        http.csrf().disable();

    	
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/api").permitAll();
    }
    

}