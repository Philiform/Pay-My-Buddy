package com.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.MoneyTransferRepository;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Transactional
@Service
public class HomeService {

	/** The internal bank account repository. */
	@Autowired
	private InternalBankAccountRepository internalBankAccountRepository;

	/** The money transfer repository. */
	@Autowired
	private MoneyTransferRepository moneyTransferRepository;

	/**
	 * Gets the amount by user id.
	 *
	 * @param userId the user id
	 * @return the amount by user id
	 */
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

	/**
	 * Gets the total commissions.
	 *
	 * @return the total commissions
	 */
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
