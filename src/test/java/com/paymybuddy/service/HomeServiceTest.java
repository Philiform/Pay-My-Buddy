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

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

	@InjectMocks
	private HomeService service;

	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	private float amount = (float) 123.45;
	private User user1;
	private InternalBankAccount internalBankAccount;

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

	@Test
	void testAmount_WhenGetAmountByUserId_ThenReturnAmount() {
		given(internalBankAccountRepository.findAmountByUserId(any(Integer.class))).willReturn(amount);

		float response = service.getAmountByUserId(1);

		verify(internalBankAccountRepository, times(1)).findAmountByUserId(any(Integer.class));
		assertThat(response).isEqualTo(amount);
	}

	@Test
	void testException_WhenGetAmountByUserId_ThenReturn0() {
		given(internalBankAccountRepository.findAmountByUserId(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		float response = service.getAmountByUserId(0);

		verify(internalBankAccountRepository, times(1)).findAmountByUserId(any(Integer.class));
		assertThat(response).isEqualTo(0);
	}

}
