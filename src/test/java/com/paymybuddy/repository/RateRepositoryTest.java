package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.paymybuddy.model.Rate;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RateRepositoryTest {

	private Rate rate;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private RateRepository repository;

	@BeforeEach
	void setUp() {
		rate = new Rate();

		rate.setRate((float) 0.8);
		rate.setDate(LocalDateTime.now());
	}

	@Test
	public void testGivenNewRate_WhenSaveRate_ThenReturnNewRate() {
		Rate savedRate = repository.save(rate);

		Rate existRate = entityManager.find(Rate.class, savedRate.getRateId());

		assertThat(rate.getDate()).isEqualTo(existRate.getDate());
	}

	@Test
	public void testGivenNewRateWithBadRateValueMin_WhenSaveRate_ThenThrowsConstraintViolationException() {
		rate.setRate((float) -0.5);

		assertThrows(ConstraintViolationException.class, () -> repository.save(rate));
	}

	@Test
	public void testGivenNewRateWithBadRateValueMax_WhenSaveRate_ThenThrowsConstraintViolationException() {
		rate.setRate((float) 1000.5);

		assertThrows(ConstraintViolationException.class, () -> repository.save(rate));
	}

	@Test
	public void testGivenModifiedRate_WhenSaveRate_ThenReturnModifiedRate() {
		Rate savedRate = repository.save(rate);

		rate.setRate((float) 0.75);
		savedRate = repository.save(rate);

		Rate existRate = entityManager.find(Rate.class, savedRate.getRateId());

		assertThat(existRate.getRate()).isEqualTo((float) 0.75);
	}

	@Test
	public void testGivenRate_WhenDeleteRate_ThenReturn1Rate() {
		Rate savedRate = repository.save(rate);

		repository.delete(rate);

		Rate existRate = entityManager.find(Rate.class, savedRate.getRateId());

		assertThat(existRate).isNull();
	}

}