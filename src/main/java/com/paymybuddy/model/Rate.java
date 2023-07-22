package com.paymybuddy.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
 * Instantiates a new rate.
 */
@NoArgsConstructor

/**
 * Instantiates a new rate.
 *
 * @param rateId the rate id
 * @param rate the rate
 * @param date the date
 */
@AllArgsConstructor
@Entity
@Table(name = "rate",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "rate", "date" })
	})
public class Rate {

	/** The rate id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rate_id")
	private int rateId;

	/** The rate. */
	@DecimalMin(value = "0.00", inclusive = true)
	@DecimalMax(value = "99.99", inclusive = true)
	@Column(name = "rate")
	private float rate;

	/** The date. */
	@DateTimeFormat
	@Column(name = "date")
	private LocalDateTime date;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "[ rateId = " + rateId + ", " +
			"rate = " + String.format("%.2f", rate) + ", " +
			"date = " + date.toString() + " ]";
	}
}
