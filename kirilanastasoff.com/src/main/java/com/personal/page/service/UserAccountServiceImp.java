package com.personal.page.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.Role;
import com.personal.page.model.UserAccount;
import com.personal.page.model.dto.UserAccountDto;
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
		Optional<UserAccount> optionalAccount = this.userAccountRepository.findById(id);
		UserAccount userAccount = null;
		if (optionalAccount.isPresent()) {
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserAccount user = userAccountRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public UserAccount saveDto(UserAccountDto userAccountDto) {
		UserAccount userAccount = new UserAccount();
		userAccount.setFirstName(userAccountDto.getFirstName());
		userAccount.setLastName(userAccountDto.getLastName());
		userAccount.setDateOfBirth(userAccountDto.getDateOfBirth());
		userAccount.setEmail(userAccountDto.getEmail());
		return this.userAccountRepository.save(userAccount);
	}

	@Override
	public UserAccount findByConfirmationToken(String confirmationToken) {
		return userAccountRepository.findByConfirmationToken(confirmationToken);
	}

}
