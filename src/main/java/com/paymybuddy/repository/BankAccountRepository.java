package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer>{

}
