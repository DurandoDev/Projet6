package com.openclassrooms.paymybuddy.controller;

import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class LoginController {

//	@RequestMapping("/*")
//	@RolesAllowed("USER")
//	public String getUser() {
//		return "redirect: /index";
//	}

	@RequestMapping("/admin")
	@RolesAllowed("ADMIN")
	public String getAdmin() {
		return "Welcome, Admin";
	}

}
