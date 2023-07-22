package com.paymybuddy.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.security.model.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

	/** The repository. */
	@Autowired
	private UserRepository repository;

	/**
	 * Load user by username.
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("==> F:loadUserByUsername");

		try {
			Optional<User> user = repository.findByEmailNotDeleted(username);

			if (user.isEmpty()) {
				throw new UsernameNotFoundException("User not found");
			}
			log.debug(user.get().toString());

			UserConnected.setUserConnected(user.get());

			return new CustomUserDetails(user.get());
		} catch (Exception e) {
			log.error("loadUserByUsername");
		}
		return new CustomUserDetails(new User());
	}

}
