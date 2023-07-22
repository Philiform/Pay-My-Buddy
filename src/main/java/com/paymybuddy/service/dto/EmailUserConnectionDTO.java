package com.paymybuddy.service.dto;

import com.paymybuddy.model.UserConnection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new email user connection DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new email user connection DTO.
 *
 * @param email the email
 * @param userConnection the user connection
 */
@AllArgsConstructor
public class EmailUserConnectionDTO {

	/** The email. */
	private String email;

	/** The user connection. */
	private UserConnection userConnection;
}
