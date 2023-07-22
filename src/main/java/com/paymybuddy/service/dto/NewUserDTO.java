package com.paymybuddy.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
 * Instantiates a new new user DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new new user DTO.
 *
 * @param firstName the first name
 * @param lastName the last name
 * @param email the email
 * @param password the password
 * @param matchingPassword the matching password
 */
@AllArgsConstructor
public class NewUserDTO {

	/** The first name. */
	@NotNull()
	@Size(min = 2, max = 50)
	private String firstName;

	/** The last name. */
	@NotNull
	@Size(min = 2, max = 50)
	private String lastName;

	/** The email. */
	@NotNull
	@Email
	@Size(min = 6, max = 50)
	private String email;

	/** The password. */
	@NotNull
	@Size(min = 6, max = 50)
	private String password;

	/** The matching password. */
	@NotNull
	@Size(min = 6, max = 50)
	private String matchingPassword;

}
