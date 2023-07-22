package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.dto.EmailUserConnectionDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionServiceTest.
 */
@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

	/** The service. */
	@InjectMocks
	private ConnectionService service;

	/** The user connection repository. */
	@Mock
	private UserConnectionRepository userConnectionRepository;

	/** The user repository. */
	@Mock
	private UserRepository userRepository;

	/** The page email user connection DTO. */
	private Page<EmailUserConnectionDTO> pageEmailUserConnectionDTO;
	
	/** The list DTO. */
	private List<EmailUserConnectionDTO> listDTO;
	
	/** The email user connection DTO 1. */
	private EmailUserConnectionDTO emailUserConnectionDTO1;
	
	/** The email user connection DTO 2. */
	private EmailUserConnectionDTO emailUserConnectionDTO2;
	
	/** The email user connection DTO 3. */
	private EmailUserConnectionDTO emailUserConnectionDTO3;
	
	/** The list user connection. */
	private List<UserConnection> listUserConnection;
	
	/** The user 1. */
	private User user1;
	
	/** The user 2. */
	private User user2;
	
	/** The user 3. */
	private User user3;
	
	/** The user connection 1. */
	private UserConnection userConnection1;
	
	/** The user connection 2. */
	private UserConnection userConnection2;
	
	/** The user connection 3. */
	private UserConnection userConnection3;
	
	/** The role. */
	private Role role;

	/** The page email user connection DTO empty. */
	private Page<EmailUserConnectionDTO> pageEmailUserConnectionDTOEmpty;
	
	/** The list DTO empty. */
	private List<EmailUserConnectionDTO> listDTOEmpty;
	
	/** The list user connection empty. */
	private List<UserConnection> listUserConnectionEmpty;

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

		userConnection1 = new UserConnection();
		userConnection1.setUserConnectionId(1);
		userConnection1.setUserSender(user1);
		userConnection1.setUserRecipient(user1);
		userConnection1.setPseudo("Me");
		user1.getUserConnections().add(userConnection1);

		user2 = new User();
		user2.setUserId(2);
		user2.setEmail("d@email.com");

		userConnection2 = new UserConnection();
		userConnection2.setUserConnectionId(2);
		userConnection2.setUserSender(user1);
		userConnection2.setUserRecipient(user2);
		userConnection2.setPseudo("His");

		user1.getUserConnections().add(userConnection2);

		listUserConnection = new ArrayList<>();
		listUserConnection.addAll(user1.getUserConnections());

		emailUserConnectionDTO1 = new EmailUserConnectionDTO();
		emailUserConnectionDTO1.setEmail(user1.getEmail());
		emailUserConnectionDTO1.setUserConnection(userConnection1);

		emailUserConnectionDTO2 = new EmailUserConnectionDTO();
		emailUserConnectionDTO2.setEmail(user2.getEmail());
		emailUserConnectionDTO2.setUserConnection(userConnection2);

		listDTO = new ArrayList<>();
		listDTO.add(emailUserConnectionDTO1);
		listDTO.add(emailUserConnectionDTO2);

//		log.info("listDTO = " + listDTO);

		pageEmailUserConnectionDTO = new PageImpl<EmailUserConnectionDTO>(listDTO);

		role = new Role();
		role.setRoleId(1);
		role.setRole("USER");

		user3 = new User();
		user3.setUserId(3);
		user3.setEmail("e@email.com");
		user3.getRoles().add(role);

		userConnection3 = new UserConnection();
		userConnection3.setUserConnectionId(0);
		userConnection3.setUserSender(user3);
		userConnection3.setUserRecipient(user3);
		userConnection3.setPseudo("Me");

		emailUserConnectionDTO3 = new EmailUserConnectionDTO();
		emailUserConnectionDTO3.setEmail(user3.getEmail());
		emailUserConnectionDTO3.setUserConnection(userConnection3);

		// EMPTY OBJECTS
		listDTOEmpty = new ArrayList<>();
		pageEmailUserConnectionDTOEmpty = new PageImpl<EmailUserConnectionDTO>(listDTOEmpty);
		listUserConnectionEmpty = new ArrayList<>();
	}

	/**
	 * Test user sender is 1 when get list email and pseudo by user sender then return page email user connection DTO.
	 */
	@Test
	void testUserSenderIs1_WhenGetListEmailAndPseudoByUserSender_ThenReturnPageEmailUserConnectionDTO() {
		given(userConnectionRepository.findByUserSenderNotDeleted(any(Integer.class))).willReturn(listUserConnection);
		given(userRepository.findById(any())).willReturn(Optional.of(user1))
			.willReturn(Optional.of(user2));

		Page<EmailUserConnectionDTO> response = service.getListEmailAndPseudoByUserSender(1, PageRequest.of(0, 5));

//		log.info("page.getContent() = " + page.getContent());

		verify(userConnectionRepository, times(1)).findByUserSenderNotDeleted(any(Integer.class));
		verify(userRepository, times(2)).findById(any());
		assertThat(response.getContent()).isEqualTo(pageEmailUserConnectionDTO.getContent());
	}

	/**
	 * Test user sender is 2 when get list email and pseudo by user sender then return page email user connection DTO empty.
	 */
	@Test
	void testUserSenderIs2_WhenGetListEmailAndPseudoByUserSender_ThenReturnPageEmailUserConnectionDTOEmpty() {
		given(userConnectionRepository.findByUserSenderNotDeleted(any(Integer.class))).willReturn(listUserConnectionEmpty);

		Page<EmailUserConnectionDTO> response = service.getListEmailAndPseudoByUserSender(1, PageRequest.of(0, 5));

		verify(userConnectionRepository, times(1)).findByUserSenderNotDeleted(any(Integer.class));
		assertThat(response.getContent()).isEqualTo(pageEmailUserConnectionDTOEmpty.getContent());
	}

	/**
	 * Test email user connection DT O when save connection then return response for operation OK.
	 */
	@Test
	void testEmailUserConnectionDTO_WhenSaveConnection_ThenReturnResponseForOperation_OK() {
		given(userRepository.findByEmail(any())).willReturn(Optional.of(user3));
		given(userRepository.getReferenceById(any())).willReturn(user3);
		given(userConnectionRepository.save(any())).willReturn(userConnection3);

//		log.info("emailUserConnectionDTO3 = " + emailUserConnectionDTO3);
//		log.info("user3 = " + user3);

		ResponseForOperation response = service.saveConnection(emailUserConnectionDTO3, user3.getUserId());

//		log.info("response = " + response);

		verify(userRepository, times(1)).findByEmail(anyString());
		verify(userRepository, times(1)).getReferenceById(any());
		verify(userConnectionRepository, times(1)).save(any());
		assertThat(response).isEqualTo(ResponseForOperation.OK);
	}

	/**
	 * Test bad role when save connection then return response for operation EMAI L INVALID.
	 */
	@Test
	void testBadRole_WhenSaveConnection_ThenReturnResponseForOperation_EMAIL_INVALID() {
		given(userRepository.findByEmail(any())).willReturn(Optional.of(user3));

		user3.getRoles().get(0).setRole("ADMIN");

		ResponseForOperation response = service.saveConnection(emailUserConnectionDTO3, user3.getUserId());

		verify(userRepository, times(1)).findByEmail(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.EMAIL_INVALID);
	}

	/**
	 * Test pseudo is empty when save connection then return response for operation PSEUD O I S EMPTY.
	 */
	@Test
	void testPseudoIsEmpty_WhenSaveConnection_ThenReturnResponseForOperation_PSEUDO_IS_EMPTY() {
		given(userRepository.findByEmail(any())).willReturn(Optional.of(user3));

		emailUserConnectionDTO3.getUserConnection().setPseudo("");

		ResponseForOperation response = service.saveConnection(emailUserConnectionDTO3, user3.getUserId());

		verify(userRepository, times(1)).findByEmail(anyString());
		assertThat(response).isEqualTo(ResponseForOperation.PSEUDO_IS_EMPTY);
	}

	/**
	 * Test exception when save connection then return response for operation ERROR.
	 */
	@Test
	void testException_WhenSaveConnection_ThenReturnResponseForOperation_ERROR() {
		given(userRepository.findByEmail(any())).willReturn(Optional.of(user3));
		given(userRepository.getReferenceById(any())).willReturn(user3);
		given(userConnectionRepository.save(any())).willAnswer(invocation -> {throw new Exception();});

		ResponseForOperation response = service.saveConnection(emailUserConnectionDTO3, user3.getUserId());

		verify(userRepository, times(1)).findByEmail(anyString());
		verify(userRepository, times(1)).getReferenceById(any());
		verify(userConnectionRepository, times(1)).save(any());
		assertThat(response).isEqualTo(ResponseForOperation.ERROR);
	}

	/**
	 * Test user connection is 2 when delete connection then delete.
	 */
	@Test
	void testUserConnectionIs2_WhenDeleteConnection_ThenDelete() {
		given(userConnectionRepository.findById(any())).willReturn(Optional.of(userConnection2));
		given(userConnectionRepository.save(any())).willReturn(userConnection2);

		emailUserConnectionDTO3.getUserConnection().setPseudo("");

		service.deleteConnection(userConnection2.getUserConnectionId());

		verify(userConnectionRepository, times(1)).findById(any());
		verify(userConnectionRepository, times(1)).save(any());
	}

	/**
	 * Test user connection unknow when delete connection then no delete.
	 */
	@Test
	void testUserConnectionUnknow_WhenDeleteConnection_ThenNoDelete() {
		given(userConnectionRepository.findById(any())).willReturn(Optional.empty());

		service.deleteConnection(10);

		verify(userConnectionRepository, times(1)).findById(any());
		verify(userConnectionRepository, times(0)).save(any());
	}

}
