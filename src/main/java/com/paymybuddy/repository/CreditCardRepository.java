package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.model.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{

}
