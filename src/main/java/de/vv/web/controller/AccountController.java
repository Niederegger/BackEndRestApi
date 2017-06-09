package de.vv.web.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> login(@RequestParam("name") String name) {
		
	}
	
}
