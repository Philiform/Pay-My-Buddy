package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.ExternalBankAccount;

public interface ExternalBankAccountRepository extends JpaRepository<ExternalBankAccount, Integer>{

	@Query(value = "SELECT external_bank_account.* FROM external_bank_account,"
			+ " user WHERE external_bank_account.external_bank_account_id ="
			+ " user.external_bank_account_id AND user.user_id = :userId", nativeQuery = true)
	Optional<ExternalBankAccount> findByUserId(final int userId);

	@Query(value = "UPDATE external_bank_account SET bank_account_id ="
			+ " :bankAccountId WHERE external_bank_account_id = :externalBankAccountId", nativeQuery = true)
	void updateBankAccount(int externalBankAccountId, int bankAccountId);

}
