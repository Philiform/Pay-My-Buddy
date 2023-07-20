package com.paymybuddy.security;

import com.paymybuddy.model.User;

public class UserConnected {

	private static User user;
	private static int id = 0;

	public static int getId() {
		return id;
	}

	public static void setUserConnected(final User newUser) {
		user = newUser;
		id = newUser.getUserId();
	}

	public static User getUserConnected() {
		return user;
	}
}
