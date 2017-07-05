package de.vv.web.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vv.web.db.DBCon;
import de.vv.web.functions.BasicFunctions;
import de.vv.web.model.RegistrationModel;
import de.vv.web.model.UserModel;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> login(@RequestParam("name") String name) {
	
		return null;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public String register(@RequestBody RegistrationModel register) {
		if(register != null && register.check()){
			register.password = BasicFunctions.encodePassword(register.password);
			System.out.println(register);
			UserModel user = DBCon.registerUser(register, "USER");
			System.out.println(user);
			return user != null && user.id > 0 ? "Success" : "Error: " + user.id;
		}
		return "Invalid RegistrationModel";
	}
	
	
}
