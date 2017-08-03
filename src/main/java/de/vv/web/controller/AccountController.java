package de.vv.web.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.vv.web.db.DBC_Account;
import de.vv.web.functions.BasicFunctions;
import de.vv.web.model.RegistrationModel;
import de.vv.web.model.user.LoginReturn;
import de.vv.web.model.user.UserModel;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public LoginReturn login(@RequestBody UserModel user) {
		System.out.println(user.toString());
		if(user != null && user.email != null){
			UserModel other = DBC_Account.getUserByEmail(user.email);
			System.out.println(other.toString());
			if(other != null && user.password.equals(other.password)){
				return new LoginReturn("Successfully logged in", other, "WOw,m8_"+other.email);
			}
		}
		return new LoginReturn("Epic fail.");
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public String register(@RequestBody RegistrationModel register) {
		if(register != null && register.check()){
			register.password = BasicFunctions.encodePassword(register.password);
			System.out.println(register);
			UserModel user = DBC_Account.registerUser(register, "USER");
			System.out.println(user);
			return user != null && user.id > 0 ? "Success" : "Error";
		}
		return "Invalid RegistrationModel";
	}
	
	
}
