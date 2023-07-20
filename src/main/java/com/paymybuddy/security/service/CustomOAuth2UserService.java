package com.paymybuddy.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.security.model.CustomOAuth2User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.debug("==> F:loadUser");

		OAuth2User oauth2User = super.loadUser(userRequest);

		log.debug("attributes = " + oauth2User.getAttributes());

		try {
			Optional<User> user = repository.findByEmailNotDeleted(oauth2User.getAttribute("email"));

			if (user.isEmpty()) {
				throw new UsernameNotFoundException("User not found");
			}
			log.debug(user.get().toString());

			UserConnected.setUserConnected(user.get());

			return new CustomOAuth2User(user.get());
		} catch (Exception e) {
		log.error("loadUserByUsername");
		}
		return new CustomOAuth2User(new User());
	}
}
