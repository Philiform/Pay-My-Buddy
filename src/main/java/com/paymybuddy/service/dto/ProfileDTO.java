package com.paymybuddy.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

	@Email
	@Size(min = 6, max = 50)
	private String email;

	@Size(min = 2, max = 50)
	private String firstName;

	@Size(min = 2, max = 50)
	private String lastName;

}
