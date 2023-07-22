package com.paymybuddy.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.CreditCard;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.ProfileService;
import com.paymybuddy.service.dto.ProfileDTO;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Controller
public class ProfileController {

	/** The service. */
	@Autowired
	private ProfileService service;

	/**
	 * Home.
	 *
	 * @param model the model
	 * @return HTML page
	 */
	@GetMapping("/user/profile")
	public String profile(Model model) {
		log.info("profile");

		try {
			int userId = UserConnected.getId();

			log.debug("user = " + String.valueOf(userId));

			Optional<ProfileDTO> profileDTO = service.getProfileByUserId(userId);
			Optional<ExternalBankAccount> externalBankAccount = service.getExternalBankAccountByUserId(userId);

			log.debug("FORMULAIRE: externalBankAccount = " + externalBankAccount.get().toString());

			model.addAttribute("menu", "profile");

			if(!profileDTO.isEmpty()) {
				model.addAttribute("profileDTO", profileDTO.get());

				if(Objects.isNull(externalBankAccount.get().getBankAccount())) {
					log.debug("FORMULAIRE: Bank account IS NULL");

					externalBankAccount.get().setBankAccount(new BankAccount());
				}

				if(Objects.isNull(externalBankAccount.get().getCreditCard())) {
					log.debug("FORMULAIRE: Credit card IS NULL");

					externalBankAccount.get().setCreditCard(new CreditCard());
				}

				model.addAttribute("externalBankAccount", externalBankAccount.get());

				log.debug("externalBankAccount = " + externalBankAccount.get());
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "profile";
	}

	/**
	 * Save profile.
	 *
	 * @param profileDTO the profile DTO
	 * @param model the model
	 * @param bindingResult the binding result
	 * @return HTML page
	 */
	@PostMapping("/user/saveProfile")
	public String saveProfile(@ModelAttribute ProfileDTO profileDTO, Model model, BindingResult bindingResult) {
		log.info("saveProfile");

		try {
			if (bindingResult.hasErrors()) return "redirect:/user/profile";

			model.addAttribute("profile", profileDTO);

			log.debug("SAVE PROFILE: first name = " + profileDTO.getFirstName() +
					", last name = " + profileDTO.getLastName());

			service.saveProfile(profileDTO);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/profile";
	}

	/**
	 * Save bank account.
	 *
	 * @param externalBankAccount the external bank account
	 * @param model the model
	 * @param bindingResult the binding result
	 * @return HTML page
	 */
	@PostMapping("/user/saveBankAccount")
	public String saveBankAccount(@ModelAttribute ExternalBankAccount externalBankAccount, Model model, BindingResult bindingResult) {
		log.info("saveBankAccount");

		try {
			if (bindingResult.hasErrors()) return "redirect:/user/profile";

			model.addAttribute("externalBankAccount", externalBankAccount);

			log.debug("SAVE BANK ACCOUNT: bank account = " + externalBankAccount.getBankAccount().getNumber());

			service.saveBankAccount(externalBankAccount);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/profile";
	}

	/**
	 * Save credit card.
	 *
	 * @param externalBankAccount the external bank account
	 * @param model the model
	 * @param bindingResult the binding result
	 * @return HTML page
	 */
	@PostMapping("/user/saveCreditCard")
	public String saveCreditCard(@ModelAttribute ExternalBankAccount externalBankAccount, Model model, BindingResult bindingResult) {
		log.info("saveCreditCard");

		try {
			if (bindingResult.hasErrors()) return "redirect:/user/profile";

			model.addAttribute("externalBankAccount", externalBankAccount);

			log.debug("SAVE CREDIT CARD: credit card = " + externalBankAccount.getCreditCard());

			service.saveCreditCard(externalBankAccount);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/profile";
	}

	/**
	 * Delete profile.
	 *
	 * @return HTML page
	 */
	@GetMapping("/user/deleteProfile")
	public String deleteProfile() {
		log.info("deleteProfile");

		try {
			log.debug("DELETE USER PROFILE");

			service.deleteProfile();
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/login";
	}

	/**
	 * Delete bank account.
	 *
	 * @param externalBankAccountId the external bank account id
	 * @return HTML page
	 */
	@GetMapping("/user/deleteBankAccount")
	public String deleteBankAccount(@RequestParam(name = "id") int externalBankAccountId) {
		log.info("deleteBankAccount");

		try {
			log.debug("DELETE BANK ACCOUNT: externalBankAccountId = " + externalBankAccountId);

			service.deleteBankAccount(externalBankAccountId);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/profile";
	}

	/**
	 * Delete credit card.
	 *
	 * @param externalBankAccountId the external bank account id
	 * @return HTML page
	 */
	@GetMapping("/user/deleteCreditCard")
	public String deleteCreditCard(@RequestParam(name = "id") int externalBankAccountId) {
		log.info("deleteCreditCard");

		try {
			log.debug("DELETE CREDIT CARD: externalBankAccountId = " + externalBankAccountId);

			service.deleteCreditCard(externalBankAccountId);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/profile";
	}

}
