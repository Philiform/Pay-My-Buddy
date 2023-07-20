package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.User;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InternalBankAccountRepositoryTest {

	private InternalBankAccount internalBankAccount;
	private User user;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private InternalBankAccountRepository repository;

	@BeforeEach
	void setUp() {
		user = new User();
		internalBankAccount = new InternalBankAccount();

		user.setEmail("h@email.com");
		user.setPassword("01234567890123456789012345678901234567890123456789");
		user.setDeleted(false);

		internalBankAccount.setUser(user);
		internalBankAccount.setBankBalance((float) 10.25);

		user.setInternalBankAccount(internalBankAccount);
	}

	@Test
	public void testGivenNewInternalBankAccount_WhenSaveinternalBankAccount_ThenReturnNewInternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class,
				savedInternalBankAccount.getInternalBankAccountId());

		assertThat(internalBankAccount.getBankBalance()).isEqualTo(existInternalBankAccount.getBankBalance());
	}

	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalanceOverMax_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) 100000.55);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalanceUnderMin_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) -1.5);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalance_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) 100000.55);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	@Test
	public void testGivenModifiedInternalBankAccount_WhenSaveInternalBankAccount_ThenReturnModifiedInternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		internalBankAccount.setBankBalance((float) 12.34);
		savedInternalBankAccount = repository.save(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class, savedInternalBankAccount.getInternalBankAccountId());

		assertThat(existInternalBankAccount.getBankBalance()).isEqualTo((float) 12.34);
	}

	@Test
	public void testGivenInternalBankAccount_WhenDeleteInternalBankAccount_ThenReturn1InternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		repository.delete(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class, savedInternalBankAccount.getInternalBankAccountId());

		assertThat(existInternalBankAccount).isNull();
	}

}