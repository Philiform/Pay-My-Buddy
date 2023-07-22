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

// TODO: Auto-generated Javadoc
/**
 * The Class CreateUserServiceTest.
 */
@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

	/** The service. */
	@InjectMocks
	private CreateUserService service;

	/** The user repository. */
	@Mock
	private UserRepository userRepository;

	/** The external bank account repository. */
	@Mock
	private ExternalBankAccountRepository externalBankAccountRepository;

	/** The internal bank account repository. */
	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	/** The role repository. */
	@Mock
	private RoleRepository roleRepository;

	/** The password encoder. */
	@Mock
	public PasswordEncoder passwordEncoder;

	/** The new user DTO. */
	private NewUserDTO newUserDTO;
	
	/** The user 1. */
	private User user1;
	
	/** The external bank account. */
	private ExternalBankAccount externalBankAccount;
	
	/** The internal bank account. */
	private InternalBankAccount internalBankAccount;
	
	/** The user connection. */
	private UserConnection userConnection;
	
	/** The role. */
	private Role role;

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

	/**
	 * Test new user DT O when save new user then return response for operation OK.
	 */
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

	/**
	 * Test email already exist when save new user then return response for operation EMAI L MUS T B E CHANGED.
	 */
	@Test
	void testEmailAlreadyExist_WhenSaveNewUser_ThenReturnResponseForOperation_EMAIL_MUST_BE_CHANGED() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.of(user1));

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.EMAIL_MUST_BE_CHANGED);
	}

	/**
	 * Test password different matching password when save new user then return response for operation ERRO R CONFIR M PASSWORD.
	 */
	@Test
	void testPasswordDifferentMatchingPassword_WhenSaveNewUser_ThenReturnResponseForOperation_ERROR_CONFIRM_PASSWORD() {
		given(userRepository.findByEmailNotDeleted(anyString())).willReturn(Optional.empty());

		newUserDTO.setMatchingPassword("");

		ResponseForOperation response = service.saveNewUser(newUserDTO);

		verify(userRepository, times(1)).findByEmailNotDeleted(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.ERROR_CONFIRM_PASSWORD);
	}

	/**
	 * Test exception when save new user then return response for operation ERROR.
	 */
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
