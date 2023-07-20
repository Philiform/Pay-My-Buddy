package com.paymybuddy.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDTO {

	@NotNull()
	@Size(min = 2, max = 50)
	private String firstName;

	@NotNull
	@Size(min = 2, max = 50)
	private String lastName;

	@NotNull
	@Email
	@Size(min = 6, max = 50)
	private String email;

	@NotNull
	@Size(min = 6, max = 50)
	private String password;

	@NotNull
	@Size(min = 6, max = 50)
	private String matchingPassword;

}
