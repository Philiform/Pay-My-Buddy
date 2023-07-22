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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
 * Instantiates a new credit card.
 */
@NoArgsConstructor

/**
 * Instantiates a new credit card.
 *
 * @param creditCardId the credit card id
 * @param number the number
 * @param expiresEndMonth the expires end month
 * @param expiresEndYear the expires end year
 * @param validationValue the validation value
 * @param externalBankAccount the external bank account
 * @param deleted the deleted
 */
@AllArgsConstructor
@Entity
@Table(name = "credit_card")
public class CreditCard {

	/** The credit card id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "credit_card_id")
	private int creditCardId;

	/** The number. */
	@Column(name = "number", length = 16)
	private String number;

	/** The expires end month. */
	@Min(1)
	@Max(12)
	@Column(name = "expires_end_month", length = 2)
	private String expiresEndMonth;

	/** The expires end year. */
	@Min(2020)
	@Max(2120)
	@Column(name = "expires_end_year", length = 4)
	private String expiresEndYear;

	/** The validation value. */
	@Min(0)
	@Max(999)
	@Column(name = "validation_value", length = 3)
	private String validationValue;

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

		return "[ creditCardId = " + creditCardId + ", " +
			"number = " + number + ", " +
			"expiresEndMonth = " + expiresEndMonth + ", " +
			"expiresEndYear = " + expiresEndYear + ", " +
			"validationValue = " + validationValue + ", " +
			"externalBankAccountexternalBankAccountId = " + eba + " ]";
	}
}
