package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.Rate;

// TODO: Auto-generated Javadoc
/**
 * The Interface RateRepository.
 */
public interface RateRepository extends JpaRepository<Rate, Integer>{

	/**
	 * Find last rate.
	 *
	 * @return the rate
	 */
	@Query(value = "SELECT * FROM Rate ORDER BY rate_id DESC LIMIT 1", nativeQuery = true)
	public Rate findLastRate();
}
