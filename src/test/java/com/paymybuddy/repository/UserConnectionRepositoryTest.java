package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;

// TODO: Auto-generated Javadoc
/**
 * The Class UserConnectionRepositoryTest.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserConnectionRepositoryTest {

	/** The user connection. */
	private UserConnection userConnection;

	/** The user sender. */
	private User userSender;

	/** The user recipient. */
	private User userRecipient;

	/** The entity manager. */
	@Autowired
	private TestEntityManager entityManager;

	/** The repository. */
	@Autowired
	private UserConnectionRepository repository;

	/**
	 * Sets the up.
	 */
	@BeforeEach
	void setUp() {
		userConnection = new UserConnection();
		userSender = new User();
		userRecipient = new User();

		userSender.setEmail("h@email.com");
		userSender.setPassword("01234567890123456789012345678901234567890123456789");
		userSender.setDeleted(false);

		userRecipient.setEmail("i@email.com");
		userRecipient.setPassword("0123456789012345678901234567890123456789012345678X");
		userRecipient.setDeleted(false);

		userConnection.setUserSender(userSender);
		userConnection.setUserRecipient(userRecipient);
		userConnection.setPseudo("Copain");
		userConnection.setDeleted(false);
	}

	/**
	 * Test given new user when save user then return new user.
	 */
	@Test
	public void testGivenNewUser_WhenSaveUser_ThenReturnNewUser() {
		UserConnection saveUserConnection = repository.save(userConnection);

		UserConnection existUserConnection = entityManager.find(UserConnection.class, saveUserConnection.getUserConnectionId());

		assertThat(userConnection.getUserConnectionId()).isEqualTo(existUserConnection.getUserConnectionId());
		assertThat(userConnection.getUserSender()).isEqualTo(existUserConnection.getUserSender());
		assertThat(userConnection.getUserRecipient()).isEqualTo(existUserConnection.getUserRecipient());
		assertThat(userConnection.getPseudo()).isEqualTo(existUserConnection.getPseudo());
		assertThat(userConnection.isDeleted()).isEqualTo(existUserConnection.isDeleted());
	}

}