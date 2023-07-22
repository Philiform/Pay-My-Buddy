package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.InternalBankAccount;

// TODO: Auto-generated Javadoc
/**
 * The Interface InternalBankAccountRepository.
 */
public interface InternalBankAccountRepository extends JpaRepository<InternalBankAccount, Integer>{

	/**
	 * Find amount by user id.
	 *
	 * @param userId the user id
	 * @return the float
	 */
	@Query(value = "SELECT internal_bank_account.bank_balance FROM internal_bank_account,"
			+ " user WHERE internal_bank_account.internal_bank_account_id ="
			+ " user.internal_bank_account_id AND user.user_id = :userId", nativeQuery = true)
	float findAmountByUserId(int userId);
}
