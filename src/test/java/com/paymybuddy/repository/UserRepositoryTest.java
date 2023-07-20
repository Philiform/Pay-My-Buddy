package com.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.paymybuddy.model.User;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

	private User user;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository repository;

	@BeforeEach
	void setUp() {
		user = new User();

		user.setEmail("h@email.com");
		user.setPassword("01234567890123456789012345678901234567890123456789");
		user.setDeleted(false);
	}

	@Test
	public void testGivenNewUser_WhenSaveUser_ThenReturnNewUser() {
		User savedUser = repository.save(user);

		User existUser = entityManager.find(User.class, savedUser.getUserId());

		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
		assertThat(user.getInternalBankAccount()).isEqualTo(existUser.getInternalBankAccount());
		assertThat(user.getUserConnections()).isEqualTo(existUser.getUserConnections());
		assertThat(user.getRoles()).isEqualTo(existUser.getRoles());
		assertThat(user.isDeleted()).isEqualTo(existUser.isDeleted());
	}

	@Test
	public void testGivenNewUserWithBadEmail_WhenSaveUser_ThenThrowsConstraintViolationException() {
		user.setEmail("hemail.com");

		assertThrows(ConstraintViolationException.class, () -> repository.save(user));
	}

	@Test
	public void testGivenNewUserWithBadPassword_WhenSaveUser_ThenThrowsConstraintViolationException() {
		user.setPassword("0123456789012345678901234567890123456789");

		assertThrows(ConstraintViolationException.class, () -> repository.save(user));
	}

	@Test
	public void testGivenEmail_WhenFindByEmail_ThenReturnNotEmptyUser() {
		Optional<User> userByEmail = repository.findByEmail("a@email.com");

		assertThat(userByEmail).isNotEmpty();
	}

	@Test
	public void testGivenEmailUnknow_WhenFindByEmail_ThenReturnEmptyUser() {
		Optional<User> userByEmail = repository.findByEmail("i@email.com");

		assertThat(userByEmail).isEmpty();
	}

	@Test
	public void testGivenModifiedUser_WhenSaveUser_ThenReturnModifiedUser() {
		User savedUser = repository.save(user);

		user.setDeleted(true);
		savedUser = repository.save(user);

		User existUser = entityManager.find(User.class, savedUser.getUserId());

		assertThat(existUser.isDeleted()).isEqualTo(true);
	}

	@Test
	public void testGivenUser_WhenDeleteUser_ThenReturn1User() {
		User savedUser = repository.save(user);

		repository.delete(user);

		User existUser = entityManager.find(User.class, savedUser.getUserId());

		assertThat(existUser).isNull();
	}

}