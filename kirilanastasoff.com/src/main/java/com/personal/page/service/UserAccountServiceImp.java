package com.personal.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.Role;
import com.personal.page.model.UserAccount;
import com.personal.page.model.dto.UserAccountDto;
import com.personal.page.repository.RoleRepository;
import com.personal.page.repository.UserAccountRepository;

@Service
public class UserAccountServiceImp implements UserDetailsService, UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public List<UserAccount> getAllUserAccounts() {
		return this.userAccountRepository.findAll();
	}

	public UserAccount findByUsername(String username) {
		return this.userAccountRepository.findByUsername(username);
	}

	@Autowired
	public UserAccountServiceImp(UserAccountRepository userAccountRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userAccountRepository = userAccountRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	//todo set username to null when update user
	public UserAccount saveUserAccount(UserAccount account) {
		account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
		account.setEnabled(true);
		account.setActive(true);
		Role userRole = roleRepository.findByRole("ADMIN");
		account.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		this.userAccountRepository.save(account);
		return account;

	}

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

	public void deleteUserAccountById(long id) {
		this.userAccountRepository.deleteById(id);
	}

	public UserAccount findByEmail(String email) {
		return this.userAccountRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userAccountRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		List<GrantedAuthority> authorities = getUserAuthority((Set<Role>) user.getRoles());
		return buildUserForAuthentication(user, authorities);
	}

	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		for (Role role : userRoles) {
			roles.add(new SimpleGrantedAuthority(role.getRole()));
		}
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
	}

	private UserDetails buildUserForAuthentication(UserAccount user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnabled(), true, true, true, authorities);
	}

	public UserAccount saveDto(UserAccountDto userAccountDto) {
		UserAccount userAccount = new UserAccount();
		userAccount.setFirstName(userAccountDto.getFirstName());
		userAccount.setLastName(userAccountDto.getLastName());
		userAccount.setDateOfBirth(userAccountDto.getDateOfBirth());
		userAccount.setEmail(userAccountDto.getEmail());
		return this.userAccountRepository.save(userAccount);
	}

	public UserAccount findByConfirmationToken(String confirmationToken) {
		return userAccountRepository.findByConfirmationToken(confirmationToken);
	}

	public UserAccount findUserByUsername(String username) {
		return userAccountRepository.findByUsername(username);
	}

	@Override
	public UserAccount findUserAccountByEmail(String email) {
		return userAccountRepository.findByEmail(email);
	}


}
