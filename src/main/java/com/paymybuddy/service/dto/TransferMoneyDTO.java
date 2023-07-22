package com.paymybuddy.service.dto;

import java.util.ArrayList;
import java.util.List;

import com.paymybuddy.model.UserConnection;

import jakarta.validation.constraints.Size;

//import java.util.function.Function;

//import com.paymybuddy.model.MoneyTransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new transfer money DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new transfer money DTO.
 *
 * @param listUserConnectionIdPseudoDTO the list user connection id pseudo DTO
 * @param userConnectionId the user connection id
 * @param amount the amount
 * @param description the description
 * @param transferFromExternal the transfer from external
 * @param transferByBankAccount the transfer by bank account
 * @param bankAccountIsPresent the bank account is present
 * @param creditCardIsPresent the credit card is present
 */
@AllArgsConstructor
public class TransferMoneyDTO {

/** The list user connection id pseudo DTO. */
//	private List<TransferUserDTO> listTransferUserDTO = new ArrayList<>();
	private List<UserConnection> listUserConnectionIdPseudoDTO = new ArrayList<>();

	/** The user connection id. */
	private int userConnectionId;

	/** The amount. */
	private int amount = 1;

	/** The description. */
	@Size(max = 50)
	private String description = "";

	// true = transferFromExternal
	/** The transfer from external. */
	// false = transferToExternal
	private boolean transferFromExternal = true;
//	private String transferFromExternal = "";

//	private String fromExternal = "fromExternal";
//
//	private String toExternal = "toExternal";

	// true = transferByBankAccount
	/** The transfer by bank account. */
// false = transferByCreditCard
	private boolean transferByBankAccount;

//	private boolean transferByCreditCard = false;

	/** The bank account is present. */
private boolean bankAccountIsPresent = false;

	/** The credit card is present. */
	private boolean creditCardIsPresent = false;
}
