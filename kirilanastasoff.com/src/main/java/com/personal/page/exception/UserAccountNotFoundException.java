package com.personal.page.exception;

public class UserAccountNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAccountNotFoundException(long id) {
		super("No usser account found by this id: " + id);
	}

}
