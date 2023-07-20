package com.paymybuddy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "external_bank_account", uniqueConstraints =
	@UniqueConstraint(columnNames = {"user_id", "bank_account_id", "credit_card_id"}))

public class ExternalBankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "external_bank_account_id")
	private int externalBankAccountId;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id", nullable = true)
	private User user = null;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "bank_account_id", nullable = true)
	private BankAccount bankAccount = null;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "credit_card_id", nullable = true)
	private CreditCard creditCard = null;

	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted = false;

	@Override
	public String toString() {
		String u = user != null ? String.valueOf(user.getUserId()) : "NULL";
		String ba = bankAccount != null ? String.valueOf(bankAccount.getBankAccountId()) : "NULL";
		String cb = creditCard != null ? String.valueOf(creditCard.getCreditCardId()) : "NULL";

		return "[ externalBankAccountId = " + externalBankAccountId + ", " +
			"user.userId = " + u + ", " +
			"bankAccount.bankAccountId = " + ba + ", " +
			"creditCard.creditCardId = " + cb + ", " +
			"deleted = " + deleted + " ]";
	}
}
