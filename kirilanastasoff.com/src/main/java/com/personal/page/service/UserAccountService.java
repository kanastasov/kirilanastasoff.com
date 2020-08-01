package com.personal.page.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.UserAccount;
import com.personal.page.model.dto.UserAccountDto;

public interface UserAccountService  {
	List<UserAccount> getAllUserAccounts();

	UserAccount saveUserAccount(UserAccount account);

	UserAccount getUserAccountById(long id) throws UserAccountNotFoundException;

	void deleteUserAccountById(long id);

	UserAccount findByEmail(String email);

	UserAccount saveDto(UserAccountDto userAccountDto);

	UserAccount findByConfirmationToken(String confirmationToken);
	
	UserAccount findUserByUsername(String username);
	
	UserAccount findUserAccountByEmail(String email);

}