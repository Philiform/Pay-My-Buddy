package com.paymybuddy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "internal_bank_account")
public class InternalBankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "internal_bank_account_id")
	private int internalBankAccountId;

	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "99999.99", inclusive = true)
	@Column(name = "bank_balance")
	private float bankBalance;

	@OneToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted;

	@Override
	public String toString() {
		String u = user != null ? String.valueOf(user.getUserId()) : "NULL";

		return "[ internalBankAccountId = " + internalBankAccountId + ", " +
			"user.userId = " + u + ", " +
			"bankBalance = " + String.format("%.2f", bankBalance) + ", " +
			"deleted = " + deleted + " ]";
	}
}
