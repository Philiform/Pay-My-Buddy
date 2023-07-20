package com.paymybuddy.service.dto;

import com.paymybuddy.model.UserConnection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailUserConnectionDTO {

	private String email;

	private UserConnection userConnection;
}
