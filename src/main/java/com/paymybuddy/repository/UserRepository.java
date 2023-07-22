package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserRepository.
 */
public interface UserRepository extends JpaRepository<User, Integer>{

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the optional
	 */
	public Optional<User> findByEmail(final String email);

	/**
	 * Find by password.
	 *
	 * @param password the password
	 * @return true, if successful
	 */
	public boolean findByPassword(final String password);

	/**
	 * Find by email contains.
	 *
	 * @param kw the kw
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<User> findByEmailContains(String kw, Pageable pageable);

	/**
	 * Find profile by user id.
	 *
	 * @param userId the user id
	 * @return the string
	 */
	@Query(value = "SELECT email, first_name, last_name FROM user WHERE user_id = :userId", nativeQuery = true)
	public String findProfileByUserId(int userId);

	/**
	 * Save profile by user id.
	 *
	 * @param emailProfile the email profile
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userId the user id
	 */
	@Modifying
	@Query(value = "UPDATE user SET email = :emailProfile, first_name = :firstName, last_name = :lastName WHERE user_id = :userId", nativeQuery = true)
	public void saveProfileByUserId(String emailProfile, String firstName, String lastName, int userId);

	/**
	 * Find by email not deleted.
	 *
	 * @param email the email
	 * @return the optional
	 */
	@Query(value = "SELECT * FROM user WHERE email = :email AND deleted = 0", nativeQuery = true)
	public Optional<User> findByEmailNotDeleted(String email);

}
