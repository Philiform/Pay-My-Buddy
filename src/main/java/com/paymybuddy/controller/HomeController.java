package com.paymybuddy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.HomeService;
import com.paymybuddy.service.ProfileService;
import com.paymybuddy.service.dto.ProfileDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	@Autowired
	private ProfileService profileService;

	@GetMapping("/user/homeUser")
	public String homeUser(Model model) {
		log.info("homeUser");

		try {
			int userId = UserConnected.getId();

			log.debug("user = " + String.valueOf(userId));

			float amount = homeService.getAmountByUserId(userId);

			Optional<ProfileDTO> profileDTO = profileService.getProfileByUserId(userId);

			model.addAttribute("menu", "homeUser");
			model.addAttribute("amount", amount);
			model.addAttribute("user", profileDTO.get());
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "homeUser";
	}

	@GetMapping("/admin/homeAdmin")
	public String homeAdmin(Model model) {
		log.info("homeAdmin");

		try {
			int userId = UserConnected.getId();

			log.debug("user = " + String.valueOf(userId));

			float commission = homeService.getTotalCommissions();

			model.addAttribute("commission", commission);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "homeAdmin";
	}

	@GetMapping("/accounting/homeAccounting")
	public String homeAccounting(Model model) {
		log.info("homeAccounting");

		try {
			int userId = UserConnected.getId();

			log.debug("user = " + String.valueOf(userId));
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "homeAccounting";
	}

}
