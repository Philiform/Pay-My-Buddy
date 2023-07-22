package com.paymybuddy.security.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.paymybuddy.model.User;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
public class CustomOAuth2User implements OAuth2User {

	/** The user. */
	private User user;

	/**
	 * Instantiates a new custom OAuth2 user.
	 *
	 * @param user the user
	 */
	public CustomOAuth2User(User user) {
		this.user = user;
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		log.debug("==> F:getAttributes");

		return new HashMap<String, Object>();
	}

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.debug("==> F:getAuthorities");

		Set<GrantedAuthority> listGrantedAuthority = new HashSet<>();

		user.getRoles().forEach(role ->
				listGrantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()))
				);
		log.debug("listGrantedAuthority = " + listGrantedAuthority.toString());

		return listGrantedAuthority;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		log.debug("==> F:getName");

		return user.getEmail();
	}

}
