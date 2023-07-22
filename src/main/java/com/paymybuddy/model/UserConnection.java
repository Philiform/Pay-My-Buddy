package com.paymybuddy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
 * Instantiates a new user connection.
 */
@NoArgsConstructor

/**
 * Instantiates a new user connection.
 *
 * @param userConnectionId the user connection id
 * @param userSender the user sender
 * @param userRecipient the user recipient
 * @param pseudo the pseudo
 * @param deleted the deleted
 */
@AllArgsConstructor
@Entity
@Table(name = "user_connection",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "user_id_sender", "user_id_recipient" })
	})
public class UserConnection {

	/** The user connection id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_connection_id")
	private int userConnectionId;

	/** The user sender. */
	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id_sender")
	private User userSender;

	/** The user recipient. */
	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id_recipient")
	private User userRecipient;

	/** The pseudo. */
	@Column(name = "pseudo", columnDefinition = "VARCHAR(50) NOT NULL DEFAULT '-'")
	@Size(max = 50)
	private String pseudo;

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
		String us = userSender != null ? String.valueOf(userSender.getUserId()) : "NULL";
		String ur = userRecipient != null ? String.valueOf(userRecipient.getUserId()) : "NULL";

		return "[ userConnectionId = " + userConnectionId + ", " +
			"userSender.userId = " + us + ", " +
			"userRecipient.userId = " + ur + ", " +
			"pseudo = " + pseudo + ", " +
			"deleted = " + deleted + " ]";
	}
}
