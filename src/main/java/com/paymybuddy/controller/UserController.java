package com.paymybuddy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.security.UserConnected;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Controller
public class UserController {

	/**
	 * Redirect home.
	 *
	 * @return HTML page
	 */
	@GetMapping("/")
	public String redirectHome() {
		log.info("redirectHome");

		String role = UserConnected.getUserConnected().getRoles().get(0).getRole();

		switch (role) {
			case "ADMIN":
				return "redirect:/admin/homeAdmin";
			case "ACCOUNTING":
				return "redirect:/accounting/homeAccounting";
			case "USER":
				return "redirect:/user/homeUser";
			default:
				return "redirect:/";
		}
	}

	/**
	 * Contact.
	 *
	 * @param model the model
	 * @return HTML page
	 */
	@GetMapping("/user/contact")
	public String contact(Model model) {
		log.info("contact");

		model.addAttribute("menu", "contact");

		return "contact";
	}

	/**
	 * Logout page.
	 *
	 * @param request the request
	 * @param response the response
	 * @return HTML page
	 */
	@GetMapping("/logoff")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		log.info("logoutPage");

		try {
			log.debug("==> F:logoutPage");

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/login?logout";
	}

}
