package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public Optional<User> findByEmail(final String email);

	public boolean findByPassword(final String password);

	public Page<User> findByEmailContains(String kw, Pageable pageable);

	@Query(value = "SELECT email, first_name, last_name FROM user WHERE user_id = :userId", nativeQuery = true)
	public String findProfileByUserId(int userId);

	@Modifying
	@Query(value = "UPDATE user SET email = :emailProfile, first_name = :firstName, last_name = :lastName WHERE user_id = :userId", nativeQuery = true)
	public void saveProfileByUserId(String emailProfile, String firstName, String lastName, int userId);

	@Query(value = "SELECT * FROM user WHERE email = :email AND deleted = 0", nativeQuery = true)
	public Optional<User> findByEmailNotDeleted(String email);

}
