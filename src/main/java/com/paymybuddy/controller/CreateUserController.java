package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.CreateUserService;
import com.paymybuddy.service.dto.NewUserDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Controller
public class CreateUserController {

	/** The service. */
	@Autowired
	private CreateUserService service;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Creates the user.
	 *
	 * @param model the model
	 * @return HTML page
	 */
	@GetMapping("/newUser/createUser")
	public String createUser(Model model) {
		log.info("createUser");

		try {
			UserConnected.setUserConnected(userRepository.getReferenceById(UserConnected.getId()));

			NewUserDTO newUserDTO = new NewUserDTO();

			model.addAttribute("profileSaved", false);
			model.addAttribute("newUserDTO", newUserDTO);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "createUser";
	}

	/**
	 * Save new user.
	 *
	 * @param newUserDTO the new user DTO
	 * @param model the model
	 * @param bindingResult the binding result
	 * @return HTML page
	 */
	@PostMapping("/newUser/saveNewUser")
	public String saveNewUser(@ModelAttribute("newUserDTO") @Valid NewUserDTO newUserDTO, Model model, BindingResult bindingResult){
		log.info("saveNewUser");

		try {
			log.debug("saveNewUser");

			if (bindingResult.hasErrors()) {
				log.error("bindingResult.hasErrors");

				model.addAttribute("message", "The information on the form is invalid");

				return "redirect:/error";
			}
			log.debug("NOT bindingResult.hasErrors");

			model.addAttribute("user", newUserDTO);

			log.debug("FORMULAIRE: first name = " + newUserDTO.getFirstName() +
					", last name = " + newUserDTO.getLastName() +
					", email = " + newUserDTO.getEmail() +
					", password = " + newUserDTO.getPassword() +
					", matchingPassword = " + newUserDTO.getMatchingPassword());

			ResponseForOperation response = service.saveNewUser(newUserDTO);
			log.debug("ResponseSaveNewUser = " + response.toString());

			if(response.equals(ResponseForOperation.OK)) {
				model.addAttribute("profileSaved", true);
				model.addAttribute("profileMessage", "Profile saved.");
			}

			else if(response.equals(ResponseForOperation.EMAIL_MUST_BE_CHANGED)) {
				model.addAttribute("profileSaved", false);
				model.addAttribute("newUserDTO", newUserDTO);
				model.addAttribute("errorMessage", "Invalid email and/or password.");
			}

			else if(response.equals(ResponseForOperation.ERROR_CONFIRM_PASSWORD)) {
				model.addAttribute("profileSaved", false);
				model.addAttribute("newUserDTO", newUserDTO);
				model.addAttribute("errorMessage", "Invalid confirmation password.");
			}

			else if(response.equals(ResponseForOperation.ERROR)) {
				model.addAttribute("errorMessage", "Error during profile recording.");
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "createUser";
	}
}
