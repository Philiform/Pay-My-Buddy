package com.paymybuddy.security.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.paymybuddy.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1983248767367474286L;

	private User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> listGrantedAuthority = new HashSet<>();

		user.getRoles().forEach(role ->
				listGrantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()))
				);
		log.debug("listGrantedAuthority = " + listGrantedAuthority.toString());

		return listGrantedAuthority;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getFullName() {
		return user.getFirstName() + " " + user.getLastName();
	}

}