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
import org.springframework.dao.DataIntegrityViolationException;

import com.paymybuddy.enumerations.InternalTypeOperation;
import com.paymybuddy.model.MoneyTransfer;
import com.paymybuddy.model.Rate;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MoneyTransferRepositoryTest {

	private MoneyTransfer moneyTransfer;
	private UserConnection userConnection;
	private User user;
	private InternalTypeOperation typeOperation;
	private Rate rate;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private MoneyTransferRepository repository;

	@BeforeEach
	void setUp() {
		moneyTransfer = new MoneyTransfer();
		userConnection = new UserConnection();
		user = new User();
		typeOperation = InternalTypeOperation.TRANSFER;
		rate = new Rate();

		rate.setRate((float)0.8);
		rate.setDate(LocalDateTime.now());

		user.setEmail("h@email.com");
		user.setPassword("01234567890123456789012345678901234567890123456789");
		user.setDeleted(false);

		userConnection.setUserSender(user);
		userConnection.setUserRecipient(user);
		userConnection.setPseudo("Copain");
		userConnection.setDeleted(false);

		moneyTransfer.setUserConnection(userConnection);
		moneyTransfer.setDate(LocalDateTime.now());
		moneyTransfer.setAmount(100);
		moneyTransfer.setInternalTypeOperation(typeOperation);
		moneyTransfer.setRate(rate);
		moneyTransfer.setDescription("Virement interne");
		moneyTransfer.setInvoice("Facture");
		moneyTransfer.setInvoiceSent(false);
	}

	@Test
	public void testGivenNewMoneyTransfer_WhenSaveMoneyTransfer_ThenReturnNewMoneyTransfer() {
		MoneyTransfer savedMoneyTransfer = repository.save(moneyTransfer);

		MoneyTransfer existMoneyTransfer = entityManager.find(MoneyTransfer.class, savedMoneyTransfer.getMoneyTransferId());

		assertThat(moneyTransfer.getMoneyTransferId()).isEqualTo(existMoneyTransfer.getMoneyTransferId());
		assertThat(moneyTransfer.getUserConnection()).isEqualTo(existMoneyTransfer.getUserConnection());
		assertThat(moneyTransfer.getDate()).isEqualTo(existMoneyTransfer.getDate());
		assertThat(moneyTransfer.getAmount()).isEqualTo(existMoneyTransfer.getAmount());
		assertThat(moneyTransfer.getInternalTypeOperation()).isEqualTo(existMoneyTransfer.getInternalTypeOperation());
		assertThat(moneyTransfer.getExternalTypeOperation()).isEqualTo(existMoneyTransfer.getExternalTypeOperation());
		assertThat(moneyTransfer.getRate()).isEqualTo(existMoneyTransfer.getRate());
		assertThat(moneyTransfer.getDescription()).isEqualTo(existMoneyTransfer.getDescription());
		assertThat(moneyTransfer.getInvoice()).isEqualTo(existMoneyTransfer.getInvoice());
		assertThat(moneyTransfer.isInvoiceSent()).isEqualTo(existMoneyTransfer.isInvoiceSent());
	}

	@Test
	public void testGivenNewMoneyTransferWithBadUserConnection_WhenSaveMoneyTransfer_ThenThrowsDataIntegrityViolationException() {
		moneyTransfer.setUserConnection(new UserConnection());

		assertThrows(DataIntegrityViolationException.class, () -> repository.save(moneyTransfer));
	}

	@Test
	public void testGivenNewMoneyTransferWithBadRateValueMin_WhenSaveRate_ThenThrowsConstraintViolationException() {
		moneyTransfer.setAmount(-10);

		assertThrows(ConstraintViolationException.class, () -> repository.save(moneyTransfer));
	}

	@Test
	public void testGivenNewMoneyTransferWithBadRateValueMax_WhenSaveRate_ThenThrowsConstraintViolationException() {
		moneyTransfer.setAmount(10000);

		assertThrows(ConstraintViolationException.class, () -> repository.save(moneyTransfer));
	}

	@Test
	public void testGivenNewMoneyTransferWithBadDescription_WhenSaveMoneyTransfer_ThenThrowsInvalidConstraintViolationException() {
		String description = "0123456789";

		moneyTransfer.setDescription(description.repeat(6));

		assertThrows(ConstraintViolationException.class, () -> repository.save(moneyTransfer));
	}

	@Test
	public void testGivenNewMoneyTransferWithBadInvoice_WhenSaveMoneyTransfer_ThenThrowsInvalidConstraintViolationException() {
		String invoice = "0123456789";

		moneyTransfer.setInvoice(invoice.repeat(60));

		assertThrows(ConstraintViolationException.class, () -> repository.save(moneyTransfer));
	}

}