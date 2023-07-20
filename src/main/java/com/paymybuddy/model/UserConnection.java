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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_connection",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "user_id_sender", "user_id_recipient" })
	})
public class UserConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_connection_id")
	private int userConnectionId;

	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id_sender")
	private User userSender;

	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinColumn(name = "user_id_recipient")
	private User userRecipient;

	@Column(name = "pseudo", columnDefinition = "VARCHAR(50) NOT NULL DEFAULT '-'")
	@Size(max = 50)
	private String pseudo;

	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted;

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
