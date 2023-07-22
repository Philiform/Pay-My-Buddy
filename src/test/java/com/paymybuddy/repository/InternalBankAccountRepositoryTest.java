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

// TODO: Auto-generated Javadoc
/**
 * The Class InternalBankAccountRepositoryTest.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InternalBankAccountRepositoryTest {

	/** The internal bank account. */
	private InternalBankAccount internalBankAccount;

	/** The user. */
	private User user;

	/** The entity manager. */
	@Autowired
	private TestEntityManager entityManager;

	/** The repository. */
	@Autowired
	private InternalBankAccountRepository repository;

	/**
	 * Sets the up.
	 */
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

	/**
	 * Test given new internal bank account when saveinternal bank account then return new internal bank account.
	 */
	@Test
	public void testGivenNewInternalBankAccount_WhenSaveinternalBankAccount_ThenReturnNewInternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class,
				savedInternalBankAccount.getInternalBankAccountId());

		assertThat(internalBankAccount.getBankBalance()).isEqualTo(existInternalBankAccount.getBankBalance());
	}

	/**
	 * Test given new internal bank account with bad bank balance over max when save internal bank account then throws constraint violation exception.
	 */
	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalanceOverMax_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) 100000.55);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	/**
	 * Test given new internal bank account with bad bank balance under min when save internal bank account then throws constraint violation exception.
	 */
	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalanceUnderMin_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) -1.5);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	/**
	 * Test given new internal bank account with bad bank balance when save internal bank account then throws constraint violation exception.
	 */
	@Test
	public void testGivenNewInternalBankAccountWithBadBankBalance_WhenSaveInternalBankAccount_ThenThrowsConstraintViolationException() {
		internalBankAccount.setBankBalance((float) 100000.55);

		assertThrows(ConstraintViolationException.class, () -> repository.save(internalBankAccount));
	}

	/**
	 * Test given modified internal bank account when save internal bank account then return modified internal bank account.
	 */
	@Test
	public void testGivenModifiedInternalBankAccount_WhenSaveInternalBankAccount_ThenReturnModifiedInternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		internalBankAccount.setBankBalance((float) 12.34);
		savedInternalBankAccount = repository.save(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class, savedInternalBankAccount.getInternalBankAccountId());

		assertThat(existInternalBankAccount.getBankBalance()).isEqualTo((float) 12.34);
	}

	/**
	 * Test given internal bank account when delete internal bank account then return 1 internal bank account.
	 */
	@Test
	public void testGivenInternalBankAccount_WhenDeleteInternalBankAccount_ThenReturn1InternalBankAccount() {
		InternalBankAccount savedInternalBankAccount = repository.save(internalBankAccount);

		repository.delete(internalBankAccount);

		InternalBankAccount existInternalBankAccount = entityManager.find(InternalBankAccount.class, savedInternalBankAccount.getInternalBankAccountId());

		assertThat(existInternalBankAccount).isNull();
	}

}