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
import com.paymybuddy.service.TransferService;
import com.paymybuddy.service.dto.TransferMoneyDTO;
import com.paymybuddy.service.dto.TransferTableDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TransferController {

	@Autowired
	private TransferService service;

	@GetMapping("/user/transfer")
	public String transfer(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		log.info("transfer");

		try {
			int userSender = UserConnected.getId();

			log.debug("user = " + String.valueOf(userSender));

			TransferMoneyDTO transferMoneyDTO = service.getNewTransferDTO(userSender);
			Page<TransferTableDTO> pageTransfer = service.getTransferByUserSender(userSender, PageRequest.of(page,size));

			model.addAttribute("menu", "transfer");
			model.addAttribute("transferMoneyDTO", transferMoneyDTO);
			model.addAttribute("listTransfer", pageTransfer.getContent());
			model.addAttribute("pages", new int[pageTransfer.getTotalPages()]);
			model.addAttribute("currentPage", page);
//			model.addAttribute("error", false);

	/*
			log.debug("\nCONTROLLER");
			log.debug("pageSize: " + PageRequest.of(page,size).getPageSize());
			log.debug("currentPage: " + PageRequest.of(page,size).getPageNumber());
			log.debug("pageTransfer: " + pageTransfer.getTotalPages());
	*/
	/*
			pageTransfer.stream().forEach((cust) -> {
				log.debug(cust.getPseudo());
				log.debug(cust.getDescription());
				log.debug(cust.getAmount());
			});
	*/
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "transfer";
	}

	@PostMapping("/user/saveTransferMoney")
	public String saveTransferMoney(@ModelAttribute TransferMoneyDTO transferMoneyDTO, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.info("saveTransferMoney");

		try {
//			log.debug("saveTransferMoney: TEST");
			if (bindingResult.hasErrors()) return "redirect:/user/transfer";

			String errorMessage = "";

			model.addAttribute("transferMoneyDTO", transferMoneyDTO);

			log.debug("saveTransferMoney: transferMoneyDTO = " + transferMoneyDTO.toString());

			ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO, UserConnected.getId());

			if(response.equals(ResponseForOperation.OK)) {
				return "redirect:/user/transfer";
			}

			else if(response.equals(ResponseForOperation.NO_BANK_ACCOUNT_OR_CREDIT_CARD)) {
				errorMessage = "You must register a bank account number or credit card in your profile.";
			}

			else if(response.equals(ResponseForOperation.BANK_BALANCE_IS_ZERO)) {
				errorMessage = "Your bank account is at €0.";
			}

			else if(response.equals(ResponseForOperation.NOT_ENOUGH_MONEY)) {
				errorMessage = "You don't have enough money to make this transfer.";
			}

			else if(response.equals(ResponseForOperation.ERROR)) {
				errorMessage = "The system has an internal error.";
			}

			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "redirect:/user/transfer?error";
	}
}
