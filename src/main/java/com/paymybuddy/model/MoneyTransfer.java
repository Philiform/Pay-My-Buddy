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

// TODO: Auto-generated Javadoc
/**
 * Hash code.
 *
 * @return the int
 */
@Data

/**
 * Instantiates a new money transfer.
 */
@NoArgsConstructor

/**
 * Instantiates a new money transfer.
 *
 * @param moneyTransferId the money transfer id
 * @param userConnection the user connection
 * @param date the date
 * @param amount the amount
 * @param commission the commission
 * @param internalTypeOperation the internal type operation
 * @param externalTypeOperation the external type operation
 * @param rate the rate
 * @param description the description
 * @param invoice the invoice
 * @param invoiceSent the invoice sent
 */
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "money_transfer")
public class MoneyTransfer {

	/** The money transfer id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "money_transfer_id")
	private int moneyTransferId;

	/** The user connection. */
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_connection_id")
	private UserConnection userConnection;

	/** The date. */
	@DateTimeFormat
	@Column(name = "date")
	private LocalDateTime date;

	/** The amount. */
	@Min(1)
	@Max(9999)
	@Column(name = "amount")
	private int amount;

	/** The commission. */
	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "99999.99", inclusive = true)
	@Column(name = "commission")
	private float commission;

	/** The internal type operation. */
	@Enumerated(EnumType.STRING)
	@Column(name = "internal_type_operation")
	private InternalTypeOperation internalTypeOperation = null;

	/** The external type operation. */
	@Enumerated(EnumType.STRING)
	@Column(name = "external_type_operation")
	private ExternalTypeOperation externalTypeOperation = null;

	/** The rate. */
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "rate_id")
	private Rate rate;

	/** The description. */
	@Column(name = "description")
	@Size(max = 50)
	private String description;

	/** The invoice. */
	@Column(name = "invoice")
	@Size(max = 500)
	private String invoice = null;

	/** The invoice sent. */
	@Column(name = "invoice_sent", length = 1, columnDefinition = "boolean default false")
	private boolean invoiceSent;

	/**
	 * To string.
	 *
	 * @return the string
	 */
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
