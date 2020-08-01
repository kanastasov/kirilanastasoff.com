package com.personal.page.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.personal.page.model.UserAccount;
import com.personal.page.repository.RoleRepository;
import com.personal.page.repository.UserAccountRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;


public class UserAccountServiceImpTest {

	@Mock
	private UserAccountRepository mockUserRepository;
	@Mock
	private RoleRepository mockRoleRepository;
	@Mock
	private BCryptPasswordEncoder mockBCryptPasswordEncoder;
	
	private UserAccountServiceImp userServiceUnderTest;
	private UserAccount user;

	@org.junit.Before
	public void setUp() {
		initMocks(this);
		userServiceUnderTest = new UserAccountServiceImp(mockUserRepository, mockRoleRepository,
				mockBCryptPasswordEncoder);
		user = UserAccount.builder().id(1).firstName("ivan").lastName("Ivanov").email("test@test.com").build();

		Mockito.when(mockUserRepository.save(any())).thenReturn(user);
		Mockito.when(mockUserRepository.findByEmail(anyString())).thenReturn(user);
	}

	@Test
	public void testFindUserByEmail() {
		// Setup
		final String email = "test@test.com";

		// Run the test
		final UserAccount result = userServiceUnderTest.findByEmail(email);

		// Verify the results
		assertEquals(email, result.getEmail());
	}

	@Test
	public void testSaveUser() {
		// Setup
		final String email = "test@test.com";

		// Run the test
		UserAccount result = userServiceUnderTest.saveUserAccount(UserAccount.builder().id(1).email("test@test.com").build());

		// Verify the results
		assertEquals(email, result.getEmail());
	}
}