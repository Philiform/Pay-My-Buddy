package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.RoleRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.dto.NewUserDTO;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

	@InjectMocks
	private CreateUserService service;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ExternalBankAccountRepository externalBankAccountRepository;

	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	public PasswordEncoder passwordEncoder;

	private NewUserDTO newUserDTO;
	private User user1;
	private ExternalBankAccount externalBankAccount;
	private InternalBankAccount internalBankAccount;
	private UserConnection userConnection;
	private Role role;

	@BeforeEach
	void setUp() throws Exception {
		user1 = new User();
		user1.setUserId(1);
		user1.setEmail("c@email.com");

		userConnection = new UserConnection();
		userConnection.setUserConnectionId(1);
		userConnection.setUserSender(user1);
		userConnection.setUserRecipient(user1);
		userConnection.setPseudo("Me");
		user1.getUserConnections().add(userConnection);

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

		newUserDTO = new NewUserDTO();
		newUserDTO.setEmail("c@email.com");
		newUserDTO.setFirstName("FirstName");
		newUserDTO.setLastName("LastName");
		newUserDTO.setPassword("123456");
		newUserDTO.setMatchingPassword("123456");
	}

	@Test
	void testNewUserDTO_WhenSaveNewUser_ThenReturnResponseForOperation_OK() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.empty());
		given(userRepository.save(any())).willReturn(user1);
		given(externalBankAccountRepository.save(any())).willReturn(externalBankAccount);
		given(internalBankAccountRepository.save(any())).willReturn(internalBankAccount);
		given(roleRepository.getReferenceById(any())).willReturn(role);

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		verify(userRepository, times(3)).save(any());
		verify(externalBankAccountRepository, times(1)).save(any());
		verify(internalBankAccountRepository, times(1)).save(any());
		verify(roleRepository, times(1)).getReferenceById(any());
		assertThat(response).isEqualTo(ResponseForOperation.OK);
	}

	@Test
	void testEmailAlreadyExist_WhenSaveNewUser_ThenReturnResponseForOperation_EMAIL_MUST_BE_CHANGED() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.of(user1));

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.EMAIL_MUST_BE_CHANGED);
	}

	@Test
	void testPasswordDifferentMatchingPassword_WhenSaveNewUser_ThenReturnResponseForOperation_ERROR_CONFIRM_PASSWORD() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.empty());

		newUserDTO.setMatchingPassword("");

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.ERROR_CONFIRM_PASSWORD);
	}

	@Test
	void testException_WhenSaveNewUser_ThenReturnResponseForOperation_ERROR() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.empty());
		given(userRepository.save(any())).willAnswer(invocation -> {throw new Exception();});

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		verify(userRepository, times(1)).save(any());
		assertThat(response).isEqualTo(ResponseForOperation.ERROR);
	}

}
