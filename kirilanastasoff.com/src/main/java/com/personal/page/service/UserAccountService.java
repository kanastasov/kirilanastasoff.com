package com.personal.page.service;

import java.util.List;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.UserAccount;

public interface UserAccountService {
	List<UserAccount> getAllUserAccounts();
	void saveUserAccount(UserAccount account);
	UserAccount getUserAccountById(long id)throws UserAccountNotFoundException;
	void deleteUserAccountById(long id);
	UserAccount findUserAccountByEmail(String email);
}
