package com.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
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

import com.paymybuddy.enumerations.ExternalTypeOperation;
import com.paymybuddy.enumerations.InternalTypeOperation;
import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.MoneyTransfer;
import com.paymybuddy.model.Rate;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.MoneyTransferRepository;
import com.paymybuddy.repository.RateRepository;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.dto.TransferMoneyDTO;
import com.paymybuddy.service.dto.TransferTableDTO;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

	/** The service. */
	@InjectMocks
	private TransferService service;

	/** The user repository. */
	@Mock
	private UserRepository userRepository;

	/** The external bank account repository. */
	@Mock
	private ExternalBankAccountRepository externalBankAccountRepository;

	/** The internal bank account repository. */
	@Mock
	private InternalBankAccountRepository internalBankAccountRepository;

	/** The user connection repository. */
	@Mock
	private UserConnectionRepository userConnectionRepository;

	/** The money transfer repository. */
	@Mock
	private MoneyTransferRepository moneyTransferRepository;

	/** The rate repository. */
	@Mock
	private RateRepository rateRepository;

	/** The page transfer table DTO. */
	private Page<TransferTableDTO> pageTransferTableDTO;
	
	/** The list DTO. */
	private List<TransferTableDTO> listDTO;
	
	/** The transfer table DTO 1. */
	private TransferTableDTO transferTableDTO1;
	
	/** The transfer table DTO 2. */
	private TransferTableDTO transferTableDTO2;
	
	/** The transfer table DTO 3. */
	private TransferTableDTO transferTableDTO3;
	
	/** The transfer money DTO 1. */
	private TransferMoneyDTO transferMoneyDTO1;
	
	/** The transfer money DTO 2. */
	private TransferMoneyDTO transferMoneyDTO2;
	
	/** The transfer money DTO 3. */
	private TransferMoneyDTO transferMoneyDTO3;
	
	/** The transfer money DTO 4. */
	private TransferMoneyDTO transferMoneyDTO4;
	
	/** The user 1. */
	private User user1;
	
	/** The user 2. */
	private User user2;
	
	/** The user 3. */
	private User user3;
	
	/** The external bank account. */
	private ExternalBankAccount externalBankAccount;
	
	/** The internal bank account 1. */
	private InternalBankAccount internalBankAccount1;
	
	/** The internal bank account 2. */
	private InternalBankAccount internalBankAccount2;
	
	/** The user connection 1. */
	private UserConnection userConnection1;
	
	/** The user connection 2. */
	private UserConnection userConnection2;
	
	/** The user connection 3. */
	private UserConnection userConnection3;
	
	/** The rate 1. */
	private Rate rate1;
	
	/** The rate 2. */
	private Rate rate2;
	
	/** The list user connection. */
	private List<UserConnection> listUserConnection;
	
	/** The list user connection id. */
	private List<Integer> listUserConnectionId;
	
	/** The list money transfer. */
	private List<MoneyTransfer> listMoneyTransfer;
	
	/** The page money transfer. */
	private Page<MoneyTransfer> pageMoneyTransfer;
	
	/** The money transfer 1. */
	private MoneyTransfer moneyTransfer1;
	
	/** The money transfer 2. */
	private MoneyTransfer moneyTransfer2;
	
	/** The money transfer 3. */
	private MoneyTransfer moneyTransfer3;

	/** The page transfer table DTO empty. */
	private Page<TransferTableDTO> pageTransferTableDTOEmpty;
	
	/** The list DTO empty. */
	private List<TransferTableDTO> listDTOEmpty;
	
	/** The transfer money DTO empty. */
	private TransferMoneyDTO transferMoneyDTOEmpty;

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

		UserConnected.setUserConnected(user1);

		externalBankAccount = new ExternalBankAccount();
		externalBankAccount.setExternalBankAccountId(1);
		externalBankAccount.setUser(user1);
		user1.setExternalBankAccount(externalBankAccount);

		internalBankAccount1 = new InternalBankAccount();
		internalBankAccount1.setInternalBankAccountId(1);
		internalBankAccount1.setUser(user1);
		internalBankAccount1.setBankBalance(10);
		user1.setInternalBankAccount(internalBankAccount1);

		userConnection1 = new UserConnection();
		userConnection1.setUserConnectionId(1);
		userConnection1.setUserSender(user1);
		userConnection1.setUserRecipient(user1);
		userConnection1.setPseudo("Me");
		user1.getUserConnections().add(userConnection1);

		user2 = new User();
		user2.setUserId(2);
		user2.setEmail("d@email.com");

		internalBankAccount2 = new InternalBankAccount();
		internalBankAccount2.setInternalBankAccountId(2);
		internalBankAccount2.setUser(user2);
		internalBankAccount2.setBankBalance(10);
		user2.setInternalBankAccount(internalBankAccount2);

		userConnection2 = new UserConnection();
		userConnection2.setUserConnectionId(2);
		userConnection2.setUserSender(user1);
		userConnection2.setUserRecipient(user2);
		userConnection2.setPseudo("His");
		user1.getUserConnections().add(userConnection2);

		user3 = new User();
		user3.setUserId(3);
		user3.setEmail("e@email.com");

		userConnection3 = new UserConnection();
		userConnection3.setUserConnectionId(3);
		userConnection3.setUserSender(user1);
		userConnection3.setUserRecipient(user3);
		userConnection3.setPseudo("Her");
		user1.getUserConnections().add(userConnection3);

		listUserConnectionId = new ArrayList<>();
		listUserConnectionId.add(userConnection1.getUserConnectionId());
		listUserConnectionId.add(userConnection2.getUserConnectionId());

//		log.info("listUserConnectionId = " + listUserConnectionId);

		listUserConnection = new ArrayList<>();
		listUserConnection.add(userConnection1);
		listUserConnection.add(userConnection2);

		rate1 = new Rate();
		rate1.setRateId(1);
		rate1.setRate(1);
		rate1.setDate(LocalDateTime.now());

		rate2 = new Rate();
		rate2.setRateId(2);
		rate2.setRate((float)1.005);
		rate2.setDate(LocalDateTime.now());

		moneyTransfer1 = new MoneyTransfer();
		moneyTransfer1.setMoneyTransferId(1);
		moneyTransfer1.setUserConnection(userConnection1);
		moneyTransfer1.setAmount(100);
		moneyTransfer1.setDate(LocalDateTime.now());
		moneyTransfer1.setRate(rate2);
		moneyTransfer1.setInternalTypeOperation(InternalTypeOperation.CREDIT);
		moneyTransfer1.setExternalTypeOperation(ExternalTypeOperation.CREDIT_CARD);
		moneyTransfer1.setDescription("");

		moneyTransfer2 = new MoneyTransfer();
		moneyTransfer2.setMoneyTransferId(2);
		moneyTransfer2.setUserConnection(userConnection2);
		moneyTransfer2.setAmount(23);
		moneyTransfer2.setDate(LocalDateTime.now());
		moneyTransfer2.setRate(rate2);
		moneyTransfer2.setInternalTypeOperation(InternalTypeOperation.TRANSFER);
		moneyTransfer2.setDescription("");

		moneyTransfer3 = new MoneyTransfer();
		moneyTransfer3.setMoneyTransferId(3);
		moneyTransfer3.setUserConnection(userConnection2);
		moneyTransfer3.setAmount(45);
		moneyTransfer3.setDate(LocalDateTime.now());
		moneyTransfer3.setRate(rate2);
		moneyTransfer3.setInternalTypeOperation(InternalTypeOperation.TRANSFER);
		moneyTransfer3.setDescription("");

		listMoneyTransfer = new ArrayList<>();
		listMoneyTransfer.add(moneyTransfer1);
		listMoneyTransfer.add(moneyTransfer2);
		listMoneyTransfer.add(moneyTransfer3);

//		log.info("listMoneyTransfer = " + listMoneyTransfer);

		pageMoneyTransfer = new PageImpl<MoneyTransfer>(listMoneyTransfer);

//		log.info("pageMoneyTransfer = " + pageMoneyTransfer);

		transferTableDTO1 = new TransferTableDTO();
		transferTableDTO1.setPseudo(userConnection1.getPseudo());
		transferTableDTO1.setAmount(moneyTransfer1.getAmount());
		transferTableDTO1.setDescription("");

		transferTableDTO2 = new TransferTableDTO();
		transferTableDTO2.setPseudo(userConnection2.getPseudo());
		transferTableDTO2.setAmount(moneyTransfer2.getAmount());
		transferTableDTO2.setDescription("");

		transferTableDTO3 = new TransferTableDTO();
		transferTableDTO3.setPseudo(userConnection2.getPseudo());
		transferTableDTO3.setAmount(moneyTransfer3.getAmount());
		transferTableDTO3.setDescription("");

		listDTO = new ArrayList<>();
		listDTO.add(transferTableDTO1);
		listDTO.add(transferTableDTO2);
		listDTO.add(transferTableDTO3);

		pageTransferTableDTO = new PageImpl<TransferTableDTO>(listDTO);

//		log.info("pageTransferTableDTO = " + pageTransferTableDTO);

		transferMoneyDTO1 = new TransferMoneyDTO();
		transferMoneyDTO1.getListUserConnectionIdPseudoDTO().addAll(listUserConnection);
		transferMoneyDTO1.setUserConnectionId(1);
		transferMoneyDTO1.setAmount(1);
		transferMoneyDTO1.setTransferByBankAccount(false);
		transferMoneyDTO1.setBankAccountIsPresent(true);

		transferMoneyDTO2 = new TransferMoneyDTO();
		transferMoneyDTO2.getListUserConnectionIdPseudoDTO().addAll(listUserConnection);
		transferMoneyDTO2.setUserConnectionId(2);
		transferMoneyDTO2.setAmount(1);
		transferMoneyDTO2.setTransferFromExternal(false);

		transferMoneyDTO3 = new TransferMoneyDTO();
		transferMoneyDTO3.getListUserConnectionIdPseudoDTO().addAll(listUserConnection);
		transferMoneyDTO3.setUserConnectionId(0);
		transferMoneyDTO3.setAmount(1);
		transferMoneyDTO3.setTransferFromExternal(true);
		transferMoneyDTO3.setTransferByBankAccount(false);
		transferMoneyDTO3.setBankAccountIsPresent(false);

		transferMoneyDTO4 = new TransferMoneyDTO();
		transferMoneyDTO4.getListUserConnectionIdPseudoDTO().addAll(listUserConnection);
		transferMoneyDTO4.setUserConnectionId(1);
		transferMoneyDTO4.setAmount(1);
		transferMoneyDTO4.setTransferFromExternal(true);
		transferMoneyDTO4.setTransferByBankAccount(false);
		transferMoneyDTO4.setBankAccountIsPresent(false);

		// EMPTY OBJECTS
		listDTOEmpty = new ArrayList<>();
		pageTransferTableDTOEmpty = new PageImpl<TransferTableDTO>(listDTOEmpty);
		transferMoneyDTOEmpty = new TransferMoneyDTO();

//		log.info("pageTransferTableDTOEmpty = " + pageTransferTableDTOEmpty.getContent());

	}

	/**
	 * Test transfer table DT O when get transfer by user sender then return page transfer table DTO.
	 */
	@Test
	void testTransferTableDTO_WhenGetTransferByUserSender_ThenReturnPageTransferTableDTO() {
		given(userConnectionRepository.findUserConnectionIdByUserSender(any(Integer.class))).willReturn(listUserConnectionId);
		given(moneyTransferRepository.findByListUserConnection(any(), any())).willReturn(pageMoneyTransfer);

		Page<TransferTableDTO> response = service.getTransferByUserSender(user1.getUserId(), PageRequest.of(0, 5));

		verify(userConnectionRepository, times(1)).findUserConnectionIdByUserSender(any(Integer.class));
		verify(moneyTransferRepository, times(1)).findByListUserConnection(any(), any());
		assertThat(response.getContent()).isEqualTo(pageTransferTableDTO.getContent());
	}

	/**
	 * Test exception when get transfer by user sender then return page transfer table DTO empty.
	 */
	@Test
	void testException_WhenGetTransferByUserSender_ThenReturnPageTransferTableDTOEmpty() {
		given(userConnectionRepository.findUserConnectionIdByUserSender(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		Page<TransferTableDTO> response = service.getTransferByUserSender(user1.getUserId(), PageRequest.of(0, 5));

		verify(userConnectionRepository, times(1)).findUserConnectionIdByUserSender(any(Integer.class));
		assertThat(response.getContent()).isEqualTo(pageTransferTableDTOEmpty.getContent());
	}

	/**
	 * Test transfer money DT O when get new transfer DT O then return transfer table DTO.
	 */
	@Test
	void testTransferMoneyDTO_WhenGetNewTransferDTO_ThenReturnTransferTableDTO() {
		given(userConnectionRepository.findByUserSenderAndNotDeletedOrderByPseudo(any(Integer.class))).willReturn(listUserConnection);
		given(externalBankAccountRepository.findByUserId(any(Integer.class))).willReturn(Optional.of(externalBankAccount));

		TransferMoneyDTO response = service.getNewTransferDTO(user1.getUserId());

		verify(userConnectionRepository, times(1)).findByUserSenderAndNotDeletedOrderByPseudo(any(Integer.class));
		verify(externalBankAccountRepository, times(1)).findByUserId(any(Integer.class));
		assertThat(response).isEqualTo(transferMoneyDTO3);
	}

	/**
	 * Test exception when get new transfer DT O then return transfer table DTO empty.
	 */
	@Test
	void testException_WhenGetNewTransferDTO_ThenReturnTransferTableDTOEmpty() {
		given(userConnectionRepository.findByUserSenderAndNotDeletedOrderByPseudo(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		TransferMoneyDTO response = service.getNewTransferDTO(user1.getUserId());

		verify(userConnectionRepository, times(1)).findByUserSenderAndNotDeletedOrderByPseudo(any(Integer.class));
		assertThat(response).isEqualTo(transferMoneyDTOEmpty);
	}

	/**
	 * Test transfer money externe when save transfer money then return response for operation OK.
	 */
	@Test
	void testTransferMoneyExterne_WhenSaveTransferMoney_ThenReturnResponseForOperation_OK() {
		given(userConnectionRepository.findById(any(Integer.class))).willReturn(Optional.of(userConnection1));
		given(rateRepository.findById(any(Integer.class))).willReturn(Optional.of(rate1));
		given(internalBankAccountRepository.getReferenceById(any(Integer.class))).willReturn(internalBankAccount1);

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO1, user1.getUserId());

		log.info("response = " + response);

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		verify(rateRepository, times(1)).findById(any(Integer.class));
		verify(internalBankAccountRepository, times(1)).getReferenceById(any(Integer.class));
		assertThat(response).isEqualTo(ResponseForOperation.OK);
	}

	/**
	 * Test transfer money interne when save transfer money then return response for operation OK.
	 */
	@Test
	void testTransferMoneyInterne_WhenSaveTransferMoney_ThenReturnResponseForOperation_OK() {
		given(userConnectionRepository.findById(any(Integer.class))).willReturn(Optional.of(userConnection2));
		given(rateRepository.findLastRate()).willReturn(rate2);
		given(internalBankAccountRepository.getReferenceById(any(Integer.class)))
			.willReturn(internalBankAccount2)
			.willReturn(internalBankAccount1);

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO2, user1.getUserId());

//		log.info("response = " + response.toString());

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		verify(rateRepository, times(1)).findLastRate();
		verify(internalBankAccountRepository, times(2)).getReferenceById(any(Integer.class));
		assertThat(response).isEqualTo(ResponseForOperation.OK);
	}

	/**
	 * Test user connection id is greater than 0 when save transfer money then return response for operation ERROR.
	 */
	@Test
	void testUserConnectionIdIsGreaterThan0_WhenSaveTransferMoney_ThenReturnResponseForOperation_ERROR() {
		transferMoneyDTOEmpty.setUserConnectionId(0);

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTOEmpty, user1.getUserId());

		assertThat(response).isEqualTo(ResponseForOperation.ERROR);
	}

	/**
	 * Test no bank account and no credit card when save transfer money then return response for operation N O BAN K ACCOUN T O R CREDI T CARD.
	 */
	@Test
	void testNoBankAccountAndNoCreditCard_WhenSaveTransferMoney_ThenReturnResponseForOperation_NO_BANK_ACCOUNT_OR_CREDIT_CARD() {
		given(userConnectionRepository.findById(any(Integer.class))).willReturn(Optional.of(userConnection1));

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO4, user1.getUserId());

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		verify(rateRepository, times(0)).findById(any(Integer.class));
		verify(rateRepository, times(0)).findLastRate();
		verify(internalBankAccountRepository, times(0)).getReferenceById(any(Integer.class));
		assertThat(response).isEqualTo(ResponseForOperation.NO_BANK_ACCOUNT_OR_CREDIT_CARD);
	}

	/**
	 * Test amount is 0 when save transfer money then return response for operation BAN K BALANC E I S ZERO.
	 */
	@Test
	void testAmountIs0_WhenSaveTransferMoney_ThenReturnResponseForOperation_BANK_BALANCE_IS_ZERO() {
		given(userConnectionRepository.findById(any(Integer.class))).willReturn(Optional.of(userConnection2));

		transferMoneyDTO2.setAmount(0);

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO2, user1.getUserId());

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		assertThat(response).isEqualTo(ResponseForOperation.BANK_BALANCE_IS_ZERO);
	}

	/**
	 * Test amount is greater than bank balance when save transfer money then return response for operation NO T ENOUG H MONEY.
	 */
	@Test
	void testAmountIsGreaterThanBankBalance_WhenSaveTransferMoney_ThenReturnResponseForOperation_NOT_ENOUGH_MONEY() {
		given(userConnectionRepository.findById(any(Integer.class))).willReturn(Optional.of(userConnection2));
		given(rateRepository.findLastRate()).willReturn(rate2);

		transferMoneyDTO2.setAmount(100);

		log.info("transferMoneyDTO2 = " + transferMoneyDTO2);

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO2, user1.getUserId());

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		verify(rateRepository, times(1)).findLastRate();
		assertThat(response).isEqualTo(ResponseForOperation.NOT_ENOUGH_MONEY);
	}

	/**
	 * Test exception when save transfer money then return response for operation ERROR.
	 */
	@Test
	void testException_WhenSaveTransferMoney_ThenReturnResponseForOperation_ERROR() {
		given(userConnectionRepository.findById(any(Integer.class))).willAnswer(invocation -> {throw new Exception();});

		ResponseForOperation response = service.saveTransferMoney(transferMoneyDTO2, user1.getUserId());

		verify(userConnectionRepository, times(1)).findById(any(Integer.class));
		assertThat(response).isEqualTo(ResponseForOperation.ERROR);
	}

}
