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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyDTO {

//	private List<TransferUserDTO> listTransferUserDTO = new ArrayList<>();
	private List<UserConnection> listUserConnectionIdPseudoDTO = new ArrayList<>();

	private int userConnectionId;

	private int amount = 1;

	@Size(max = 50)
	private String description = "";

	// true = transferFromExternal
	// false = transferToExternal
	private boolean transferFromExternal = true;
//	private String transferFromExternal = "";

//	private String fromExternal = "fromExternal";
//
//	private String toExternal = "toExternal";

	// true = transferByBankAccount
	// false = transferByCreditCard
	private boolean transferByBankAccount;

//	private boolean transferByCreditCard = false;

	private boolean bankAccountIsPresent = false;

	private boolean creditCardIsPresent = false;
}
