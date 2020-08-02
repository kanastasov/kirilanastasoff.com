package com.personal.page.exception;

public class UserAccountException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public UserAccountException() {
		super();
	}

	public UserAccountException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public UserAccountException(long id) {
		super("No usser account found by this id: " + id);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	

}
