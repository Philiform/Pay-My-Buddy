package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.CreditCard;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.repository.CreditCardRepository;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.dto.ProfileDTO;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

	@InjectMocks
	private ProfileService service;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ExternalBankAccountRepository externalBankAccountRepository;

	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	@Mock
	private BankAccountRepository bankAccountRepository;

	@Mock
	private CreditCardRepository creditCardRepository;

	@Mock
	private UserConnectionRepository userConnectionRepository;

	private ProfileDTO profileDTO;
	private String profile;
	private User user1;
	private ExternalBankAccount externalBankAccount;
	private InternalBankAccount internalBankAccount;
	private UserConnection userConnection;
	private BankAccount bankAccount;
	private CreditCard creditCard;
	private Role role;
	private List<UserConnection> listUserConnection;

	@BeforeEach
	void setUp() throws Exception {
		user1 = new User();
		user1.setUserId(1);
		user1.setEmail("c@email.com");

		UserConnected.setUserConnected(user1);
		userConnection = new UserConnection();
		userConnection.setUserConnectionId(1);
		userConnection.setUserSender(user1);
		userConnection.setUserRecipient(user1);
		userConnection.setPseudo("Me");
		user1.getUserConnections().add(userConnection);

		listUserConnection = new ArrayList<>();
		listUserConnection.add(userConnection);

		role = new Role();
		role.setRoleId(1);
		role.setRole("USER");
		user1.getRoles().add(role);

		externalBankAccount = new ExternalBankAccount();
		externalBankAccount.setExternalBankAccountId(1);
		externalBankAccount.setUser(user1);
		user1.setExternalBankAccount(externalBankAccount);

		internalBankAccount = new InternalBankAccount();
		internalBankAccount.setInternalBankAccountId(1);
		internalBankAccount.setUser(user1);
		user1.setInternalBankAccount(internalBankAccount);

		profileDTO = new ProfileDTO();
		profileDTO.setEmail("c@email.com");
		profileDTO.setFirstName("FirstName");
		profileDTO.setLastName("LastName");

		profile = "c@email.com,FirstName,LastName";

		bankAccount = new BankAccount();
		bankAccount.setBankAccountId(0);
		bankAccount.setExternalBankAccount(externalBankAccount);

		externalBankAccount.setBankAccount(bankAccount);

		creditCard = new CreditCard();
		creditCard.setCreditCardId(0);
		creditCard.setExternalBankAccount(externalBankAccount);

		externalBankAccount.setCreditCard(creditCard);
	}

	// Profile
	@Test
	void testProfileDTO_WhenGetProfileByUserId_ThenReturnOptionalProfileDTO() {
		given(userRepository.findProfileByUserId(any(Integer.class))).willReturn(profile);

		Optional<ProfileDTO> response = service.getProfileByUserId(1);

		verify(userRepository, times(1)).findProfileByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.of(profileDTO));
	}

	@Test
	void testUserUnknow_WhenGetProfileByUserId_ThenReturnOptionalEmpty() {
		given(userRepository.findProfileByUserId(any(Integer.class))).willReturn("");

		Optional<ProfileDTO> response = service.getProfileByUserId(20);

		verify(userRepository, times(1)).findProfileByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.empty());
	}

	@Test
	void testException_WhenGetProfileByUserId_ThenReturnOptionalEmpty() {
		given(userRepository.findProfileByUserId(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		Optional<ProfileDTO> response = service.getProfileByUserId(1);

		verify(userRepository, times(1)).findProfileByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.empty());
	}

	@Test
	void testProfileDTO_WhenSaveProfile_ThenReturnOptionalProfileDTO() {
		given(userRepository.findProfileByUserId(any(Integer.class))).willReturn(profile);

		Optional<ProfileDTO> response = service.getProfileByUserId(1);

		verify(userRepository, times(1)).findProfileByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.of(profileDTO));
	}

	@Test
	void testProfile_WhenDeleteProfile_ThenDeleteProfile() {
		given(userConnectionRepository.findToDelete(any(Integer.class))).willReturn(listUserConnection);
		given(userConnectionRepository.save(any())).willReturn(userConnection);
		given(externalBankAccountRepository.findById(any(Integer.class))).willReturn(Optional.of(externalBankAccount));
		given(bankAccountRepository.save(any())).willReturn(bankAccount);
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);
		given(creditCardRepository.save(any())).willReturn(creditCard);
		given(externalBankAccountRepository.getReferenceById(any(Integer.class))).willReturn(externalBankAccount);
		given(internalBankAccountRepository.getReferenceById(any(Integer.class))).willReturn(internalBankAccount);
		given(userRepository.save(any())).willReturn(user1);

		service.deleteProfile();

		verify(userConnectionRepository, times(1)).findToDelete(any(Integer.class));
		verify(userConnectionRepository, times(1)).save(any());
		verify(externalBankAccountRepository, times(2)).findById(any(Integer.class));
		verify(bankAccountRepository, times(1)).save(any());
		verify(externalBankAccountRepository, times(3)).save(any());
		verify(creditCardRepository, times(1)).save(any());
		verify(externalBankAccountRepository, times(1)).getReferenceById(any(Integer.class));
		verify(internalBankAccountRepository, times(1)).getReferenceById(any(Integer.class));
		verify(userRepository, times(1)).save(any());
	}

	// ExternalBankAccount
	@Test
	void testExternalBankAccount_WhenGetExternalBankAccountByUserId_ThenReturnOptionalExternalBankAccount() {
		given(externalBankAccountRepository.findByUserId(any(Integer.class))).willReturn(Optional.of(externalBankAccount));

		Optional<ExternalBankAccount> response = service.getExternalBankAccountByUserId(1);

		verify(externalBankAccountRepository, times(1)).findByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.of(externalBankAccount));
	}

	@Test
	void testExternalBankAccountUnknow_WhenGetExternalBankAccountByUserId_ThenReturnOptionalEmpty() {
		given(externalBankAccountRepository.findByUserId(any(Integer.class))).willReturn(Optional.empty());

		Optional<ExternalBankAccount> response = service.getExternalBankAccountByUserId(1);

		verify(externalBankAccountRepository, times(1)).findByUserId(any(Integer.class));
		assertThat(response).isEqualTo(Optional.empty());
	}

	// BankAccount
	@Test
	void testBankAccountIdIs0_WhenSaveBankAccount_ThenReturnOptionalBankAccount() {
		given(bankAccountRepository.save(any())).willReturn(bankAccount);
		given(externalBankAccountRepository.findById(any(Integer.class))).willReturn(Optional.of(externalBankAccount));
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);

		Optional<BankAccount> response = service.saveBankAccount(externalBankAccount);

		verify(bankAccountRepository, times(2)).save(any());
		verify(externalBankAccountRepository, times(1)).findById(any(Integer.class));
		verify(externalBankAccountRepository, times(1)).save(any());
		assertThat(response).isEqualTo(Optional.of(bankAccount));
	}

	@Test
	void testException_WhenSaveBankAccount_ThenReturnOptionalEmpty() {
		given(bankAccountRepository.save(any())).willAnswer(invocation -> {throw new Exception();});

		Optional<BankAccount> response = service.saveBankAccount(externalBankAccount);

		verify(bankAccountRepository, times(1)).save(any());
		assertThat(response).isEqualTo(Optional.empty());
	}

	@Test
	void testBankAccount_WhenDeleteBankAccount_ThenDeleteBankAccount() {
		given(externalBankAccountRepository.findById(any(Integer.class))).willReturn(Optional.of(externalBankAccount));
		given(bankAccountRepository.save(any())).willReturn(bankAccount);
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);

		service.deleteBankAccount(externalBankAccount.getExternalBankAccountId());

		verify(externalBankAccountRepository, times(1)).findById(any(Integer.class));
		verify(bankAccountRepository, times(1)).save(any());
		verify(externalBankAccountRepository, times(1)).save(any());
	}

	// CreditCard
	@Test
	void testCreditCardIdIs0_WhenSaveCreditCard_ThenReturnOptional() {
		given(creditCardRepository.save(any())).willReturn(creditCard);
		given(externalBankAccountRepository.findById(any(Integer.class))).willReturn(Optional.of(externalBankAccount));
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);

		Optional<CreditCard> response = service.saveCreditCard(externalBankAccount);

		verify(creditCardRepository, times(2)).save(any());
		verify(externalBankAccountRepository, times(1)).findById(any(Integer.class));
		verify(externalBankAccountRepository, times(1)).save(any());
		assertThat(response).isEqualTo(Optional.of(creditCard));
	}

	@Test
	void testException_WhenSaveCreditCard_ThenReturnOptionalEmpty() {
		given(creditCardRepository.save(any())).willAnswer(invocation -> {throw new Exception();});

		Optional<CreditCard> response = service.saveCreditCard(externalBankAccount);

		verify(creditCardRepository, times(1)).save(any());
		assertThat(response).isEqualTo(Optional.empty());
	}

	@Test
	void testCreditCard_WhenDeleteCreditCard_ThenDeleteCreditCard() {

		given(externalBankAccountRepository.findById(any(Integer.class))).willReturn(Optional.of(externalBankAccount));
		given(creditCardRepository.save(any())).willReturn(creditCard);
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);

		service.deleteCreditCard(externalBankAccount.getExternalBankAccountId());

		verify(externalBankAccountRepository, times(1)).findById(any(Integer.class));
		verify(creditCardRepository, times(1)).save(any());
		verify(externalBankAccountRepository, times(1)).save(any());
	}

}
