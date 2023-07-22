package com.paymybuddy.security;

import com.paymybuddy.model.User;

// TODO: Auto-generated Javadoc
/**
 * The Class UserConnected.
 */
public class UserConnected {

	/** The user. */
	private static User user;
	
	/** The id. */
	private static int id = 0;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public static int getId() {
		return id;
	}

	/**
	 * Sets the user connected.
	 *
	 * @param newUser the new user connected
	 */
	public static void setUserConnected(final User newUser) {
		user = newUser;
		id = newUser.getUserId();
	}

	/**
	 * Gets the user connected.
	 *
	 * @return the user connected
	 */
	public static User getUserConnected() {
		return user;
	}
}
