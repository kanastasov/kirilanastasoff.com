package com.personal.page.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.UserAccount;
import com.personal.page.repository.UserAccountRepository;

@Service
public class UserAccountServiceImp implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Override
	public List<UserAccount> getAllUserAccounts() {
		return this.userAccountRepository.findAll();
	}

	@Override
	public void saveUserAccount(UserAccount account) {
		this.userAccountRepository.save(account);

	}

	@Override
	public UserAccount getUserAccountById(long id) throws UserAccountNotFoundException {
		Optional<UserAccount> optionalAccount =  this.userAccountRepository.findById(id);
		UserAccount userAccount = null;
		if(optionalAccount.isPresent()) {
			userAccount = optionalAccount.get();
		} else {
			throw new UserAccountNotFoundException(id);
		}
		
		return userAccount;
	}

	@Override
	public void deleteUserAccountById(long id) {
		this.userAccountRepository.deleteById(id);
	}

	@Override
	public UserAccount findUserAccountByEmail(String email) {
		return this.userAccountRepository.findByEmail(email);
	}

}
