package com.paymybuddy.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.paymybuddy.enumerations.ExternalTypeOperation;
import com.paymybuddy.enumerations.InternalTypeOperation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "money_transfer")
public class MoneyTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "money_transfer_id")
	private int moneyTransferId;

	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_connection_id")
	private UserConnection userConnection;

	@DateTimeFormat
	@Column(name = "date")
	private LocalDateTime date;

	@Min(1)
	@Max(9999)
	@Column(name = "amount")
	private int amount;

	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "99999.99", inclusive = true)
	@Column(name = "commission")
	private float commission;

	@Enumerated(EnumType.STRING)
	@Column(name = "internal_type_operation")
	private InternalTypeOperation internalTypeOperation = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "external_type_operation")
	private ExternalTypeOperation externalTypeOperation = null;

	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "rate_id")
	private Rate rate;

	@Column(name = "description")
	@Size(max = 50)
	private String description;

	@Column(name = "invoice")
	@Size(max = 500)
	private String invoice = null;

	@Column(name = "invoice_sent", length = 1, columnDefinition = "boolean default false")
	private boolean invoiceSent;

	@Override
	public String toString() {
		String uc = userConnection != null ? String.valueOf(userConnection.getUserConnectionId()) : "NULL";
		String ito = internalTypeOperation != null ? String.valueOf(internalTypeOperation.toString()) : "NULL";
		String eto = externalTypeOperation != null ? String.valueOf(externalTypeOperation.toString()) : "NULL";
		String r = rate != null ? String.valueOf(rate.getRateId()) : "NULL";

		return "[ moneyTransferId = " + moneyTransferId + ", " +
			"userConnection.userConnectionId = " + uc + ", " +
			"date = " + date + ", " +
			"amount = " + String.valueOf(amount) + ", " +
			"internalTypeOperation = " + ito + ", " +
			"externalTypeOperation = " + eto + ", " +
			"rate.rate_id = " + r + ", " +
			"description = " + description + ", " +
			"invoice = " + invoice + ", " +
			"invoiceSent = " + invoiceSent + " ]";
	}
}
