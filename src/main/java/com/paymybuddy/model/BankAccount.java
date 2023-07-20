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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * Hash code.
 *
 * @return the int
 */
@Data

/**
 * Instantiates a new bank account.
 */
@NoArgsConstructor

/**
 * Instantiates a new bank account.
 *
 * @param bankAccountId the bank account id
 * @param number the number
 * @param externalBankAccount the external bank account
 * @param deleted the deleted
 */
@AllArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccount {

	/** The bank account id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bank_account_id")
	private int bankAccountId;

	/** The number. */
	@Column(name = "number", length = 27, unique = true)
	private String number;

	/** The external bank account. */
	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "external_bank_account_id", unique = false)
	private ExternalBankAccount externalBankAccount;

	/** The deleted. */
	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String eba = externalBankAccount != null ? String.valueOf(externalBankAccount.getExternalBankAccountId()) : "NULL";

		return "[ bankAccountId = " + bankAccountId + ", " +
			"number = " + number + ", " +
			"externalBankAccountexternalBankAccountId = " + eba + " ]";
	}
}
