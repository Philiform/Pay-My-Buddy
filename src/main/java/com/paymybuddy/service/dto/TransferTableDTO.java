package com.paymybuddy.service.dto;

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
 * Instantiates a new transfer table DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new transfer table DTO.
 *
 * @param pseudo the pseudo
 * @param description the description
 * @param amount the amount
 */
@AllArgsConstructor
public class TransferTableDTO {

	/** The pseudo. */
	private String pseudo;

	/** The description. */
	private String description;

	/** The amount. */
	private int amount;
}
