package com.paymybuddy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.paymybuddy.security.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private CustomOAuth2UserService userOAuthService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		System.out.println("nouveau mot de passe (000000) = " + passwordEncoder().encode("000000"));

		http.formLogin((form) -> form
				.loginPage("/login")
				.loginProcessingUrl("/login/authentication")
				.permitAll()
				.usernameParameter("email")
				.defaultSuccessUrl("/")
				.successHandler(loginSuccessHandler)
				.failureUrl("/login?error"));

		http.oauth2Login((oaut) -> oaut
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.successHandler(loginSuccessHandler)
				.userInfoEndpoint((userInfo) -> userInfo
					.userService(userOAuthService)));

		http.rememberMe((remember) -> remember
				.key("rememberKey")
				.rememberMeParameter("remember")
				.tokenValiditySeconds(86400));

		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/accounting/**").hasRole("ACCOUNTING")
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/webjars/**").permitAll()
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/newUser/**").anonymous()
				.anyRequest()
				.authenticated());

		http.logout((logout) -> logout
				.permitAll()
//				.logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID"));

		http.exceptionHandling((exception) -> exception.accessDeniedPage("/"));

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
