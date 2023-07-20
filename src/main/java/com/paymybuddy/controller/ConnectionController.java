package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.dto.EmailUserConnectionDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ConnectionController {
	@Autowired
	private ConnectionService service;

	@GetMapping("/user/transfer/connection")
	public String connection(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		log.info("connection");

		try {
			int userSender = UserConnected.getId();

			Page<EmailUserConnectionDTO> pageEmailPseudo = service.getListEmailAndPseudoByUserSender(userSender, PageRequest.of(page, size));


			if(model.getAttribute("emailUserConnectionDTO") != null) {
				EmailUserConnectionDTO dto = (EmailUserConnectionDTO) model.getAttribute("emailUserConnectionDTO");

				if(dto.getEmail() == null) {
					model.addAttribute("emailUserConnectionDTO", new EmailUserConnectionDTO());
				}
			} else {
				model.addAttribute("emailUserConnectionDTO", new EmailUserConnectionDTO());
			}

			model.addAttribute("listEmailPseudo", pageEmailPseudo.getContent());
			model.addAttribute("pages", new int[pageEmailPseudo.getTotalPages()]);
			model.addAttribute("currentPage", page);
	/*
			pageEmailPseudo.stream().forEach((cust) -> {
				log.debug(cust.getEmail());
				log.debug(cust.getPseudo());
			});
	*/
		} catch (Exception e) {
			log.error(e.toString());
		}
		return "connection";
	}

	@PostMapping("/user/transfer/saveConnection")
	public String saveConnection(@ModelAttribute EmailUserConnectionDTO emailUserConnectionDTO, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.info("saveConnection");

		try {
			if (bindingResult.hasErrors()) return "redirect:/user/transfer/connection";

			String errorMessage = "";

			log.debug("FORMULAIRE: emailUserConnectionDTO = " + emailUserConnectionDTO.toString());

			ResponseForOperation response = service.saveConnection(emailUserConnectionDTO, UserConnected.getId());

			if(response.equals(ResponseForOperation.OK)) {
				return "redirect:/user/transfer/connection";
			}

			else if(response.equals(ResponseForOperation.EMAIL_INVALID)) {
				errorMessage = "Invalid email.";
			}

			else if(response.equals(ResponseForOperation.PSEUDO_IS_EMPTY)) {
				errorMessage = "You must choose a nickname.";
			}

			else if(response.equals(ResponseForOperation.ERROR)) {
				errorMessage = "Error during connection recording.";
			}

			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			redirectAttributes.addAttribute("emailUserConnectionDTO", emailUserConnectionDTO);
//			model.addAttribute("errorMessage", errorMessage);

		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/transfer/connection?error";
//		return "connection";
	}

	@GetMapping("/user/transfer/deleteConnection")
	public String deleteConnection(@RequestParam(name = "id") int userConnectionId/*, Model model*/) {
		log.info("deleteConnection");

		try {
			log.debug("DELETE CONNECTION: UserConnectionId = " + userConnectionId);

			service.deleteConnection(userConnectionId);
			//		PseudoUserConnectionDTO pseudoUserConnectionDTO = service.getUserConnectionById(id);
		//
		//		model.addAttribute("pseudoUserConnectionDTO", pseudoUserConnectionDTO);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/transfer/connection";
	}

}
