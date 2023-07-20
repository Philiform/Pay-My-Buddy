package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.InternalBankAccount;

public interface InternalBankAccountRepository extends JpaRepository<InternalBankAccount, Integer>{

	@Query(value = "SELECT internal_bank_account.bank_balance FROM internal_bank_account,"
			+ " user WHERE internal_bank_account.internal_bank_account_id ="
			+ " user.internal_bank_account_id AND user.user_id = :userId", nativeQuery = true)
	float findAmountByUserId(int userId);
}
