package com.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.MoneyTransferRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class HomeService {

	@Autowired
	private InternalBankAccountRepository internalBankAccountRepository;

	@Autowired
	private MoneyTransferRepository moneyTransferRepository;

	public float getAmountByUserId(final int userId) {
		float amount = 0;

		try {
			amount = internalBankAccountRepository.findAmountByUserId(userId);
		} catch (Exception e) {
			log.error("amount: NOT VALUE");
			log.error(e.toString());
		}

		return amount;
	}

	public float getTotalCommissions() {
		float commission = 0;

		try {
			commission = moneyTransferRepository.getTotalCommissions();
		} catch (Exception e) {
			log.error(e.toString());
		}

		return commission;
	}
}
