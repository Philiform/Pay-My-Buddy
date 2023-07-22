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
import org.springframework.dao.DataIntegrityViolationException;

import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;

// TODO: Auto-generated Javadoc
/**
 * The Class RoleRepositoryTest.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTest {

	/** The role. */
	private Role role;

	/** The user. */
	private User user;

	/** The entity manager. */
	@Autowired
	private TestEntityManager entityManager;

	/** The repository. */
	@Autowired
	private RoleRepository repository;

	/**
	 * Sets the up.
	 */
	@BeforeEach
	void setUp() {
		role = new Role();
		user = new User();

		user.setEmail("h@email.com");
		user.setPassword("01234567890123456789012345678901234567890123456789");
		user.setDeleted(false);

		role.setRole("User2");
		role.getUsers().add(user);

		user.getRoles().add(role);
	}

	/**
	 * Test given new role when save role then return new role.
	 */
	@Test
	public void testGivenNewRole_WhenSaveRole_ThenReturnNewRole() {
		Role savedRole = repository.save(role);

		Role existRole = entityManager.find(Role.class, savedRole.getRoleId());

		assertThat(role.getRole()).isEqualTo(existRole.getRole());
		assertThat(role.getUsers().size()).isEqualTo(existRole.getUsers().size());
	}

	/**
	 * Test given role already exist when save role then throws data integrity violation exception.
	 */
	@Test
	public void testGivenRoleAlreadyExist_WhenSaveRole_ThenThrowsDataIntegrityViolationException() {
		role.setRole("Admin");

		assertThrows(DataIntegrityViolationException.class, () -> repository.save(role));
	}

}