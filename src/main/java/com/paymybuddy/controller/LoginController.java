package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Controller
public class LoginController {

	/**
	 * Login.
	 *
	 * @return HTML page
	 */
	@GetMapping("/login")
	public String login() {
		log.info("login");

		return "login";
	}

}
