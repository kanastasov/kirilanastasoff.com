package com.personal.page.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.page.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	UserAccount findByEmail(String email);

	UserAccount findByUsername(String username);

	UserAccount findByConfirmationToken(String confirmationToken);
}
