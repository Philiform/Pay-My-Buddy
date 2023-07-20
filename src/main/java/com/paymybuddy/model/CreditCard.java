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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credit_card")
public class CreditCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "credit_card_id")
	private int creditCardId;

	@Column(name = "number", length = 16)
	private String number;

	@Min(1)
	@Max(12)
	@Column(name = "expires_end_month", length = 2)
	private String expiresEndMonth;

	@Min(2020)
	@Max(2120)
	@Column(name = "expires_end_year", length = 4)
	private String expiresEndYear;

	@Min(0)
	@Max(999)
	@Column(name = "validation_value", length = 3)
	private String validationValue;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "external_bank_account_id", unique = false)
	private ExternalBankAccount externalBankAccount;

	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted;

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
