package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.InternalBankAccountRepository;

// TODO: Auto-generated Javadoc
//import lombok.extern.slf4j.Slf4j;

/**
 * The Class HomeServiceTest.
 */
//@Slf4j
@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

	/** The service. */
	@InjectMocks
	private HomeService service;

	/** The internal bank account repository. */
	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	/** The amount. */
	private float amount = (float) 123.45;
	
	/** The user 1. */
	private User user1;
	
	/** The internal bank account. */
	private InternalBankAccount internalBankAccount;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		user1 = new User();
		user1.setUserId(1);
		user1.setEmail("c@email.com");

		internalBankAccount = new InternalBankAccount();
		internalBankAccount.setInternalBankAccountId(1);
		internalBankAccount.setUser(user1);
		user1.setInternalBankAccount(internalBankAccount);

	}

	/**
	 * Test amount when get amount by user id then return amount.
	 */
	@Test
	void testAmount_WhenGetAmountByUserId_ThenReturnAmount() {
		given(internalBankAccountRepository.findAmountByUserId(any(Integer.class))).willReturn(amount);

		float response = service.getAmountByUserId(1);

		verify(internalBankAccountRepository, times(1)).findAmountByUserId(any(Integer.class));
		assertThat(response).isEqualTo(amount);
	}

	/**
	 * Test exception when get amount by user id then return 0.
	 */
	@Test
	void testException_WhenGetAmountByUserId_ThenReturn0() {
		given(internalBankAccountRepository.findAmountByUserId(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		float response = service.getAmountByUserId(0);

		verify(internalBankAccountRepository, times(1)).findAmountByUserId(any(Integer.class));
		assertThat(response).isEqualTo(0);
	}

}
