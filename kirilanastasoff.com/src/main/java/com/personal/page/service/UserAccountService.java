package com.personal.page.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.UserAccount;
import com.personal.page.model.dto.UserAccountDto;

public interface UserAccountService extends UserDetailsService {
	List<UserAccount> getAllUserAccounts();

	void saveUserAccount(UserAccount account);

	UserAccount getUserAccountById(long id) throws UserAccountNotFoundException;

	void deleteUserAccountById(long id);

	UserAccount findUserAccountByEmail(String email);

	UserAccount saveDto(UserAccountDto userAccountDto);

	UserAccount findByConfirmationToken(String confirmationToken);
}
