package com.paymybuddy.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
 * Instantiates a new profile DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new profile DTO.
 *
 * @param email the email
 * @param firstName the first name
 * @param lastName the last name
 */
@AllArgsConstructor
public class ProfileDTO {

	/** The email. */
	@Email
	@Size(min = 6, max = 50)
	private String email;

	/** The first name. */
	@Size(min = 2, max = 50)
	private String firstName;

	/** The last name. */
	@Size(min = 2, max = 50)
	private String lastName;

}
