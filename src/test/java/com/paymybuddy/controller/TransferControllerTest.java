package com.paymybuddy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.service.TransferService;
import com.paymybuddy.service.dto.TransferMoneyDTO;
import com.paymybuddy.service.dto.TransferTableDTO;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TransferControllerTest {

	@Autowired
	private TransferController controller;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	public TransferService service;

	private static Page<TransferTableDTO> pageTransferTableDTO;
	private static List<TransferTableDTO> listDTO;
	private static TransferTableDTO transferTableDTO;
	private static TransferMoneyDTO transferMoneyDTO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		transferMoneyDTO = new TransferMoneyDTO();
		transferMoneyDTO.setAmount(10);
		transferMoneyDTO.setBankAccountIsPresent(false);
		transferMoneyDTO.setCreditCardIsPresent(false);
		transferMoneyDTO.setListUserConnectionIdPseudoDTO(new ArrayList<UserConnection>());
		transferMoneyDTO.setTransferByBankAccount(false);
		transferMoneyDTO.setTransferFromExternal(false);
		transferMoneyDTO.setUserConnectionId(3);

		transferTableDTO = new TransferTableDTO();
		transferTableDTO.setAmount(10);
		transferTableDTO.setDescription("");
		transferTableDTO.setPseudo("Pseudo");

		listDTO = new ArrayList<>();
		listDTO.add(transferTableDTO);

		pageTransferTableDTO = new PageImpl<TransferTableDTO>(listDTO);

	}

	@Test
	public void testGivenController_ThenReturnNotNull() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testGivenGetTransfer_ThenReturnOk() throws Exception {
		given(service.getNewTransferDTO(any(Integer.class))).willReturn(transferMoneyDTO);
		given(service.getTransferByUserSender(any(Integer.class), any())).willReturn(pageTransferTableDTO);

		mockMvc
			.perform(get("/user/transfer"))
			.andExpect(status().isOk())
			.andExpect(view().name("transfer"))
			.andExpect(model().attributeExists("transferMoneyDTO", "listTransfer"));

		verify(service, times(1)).getNewTransferDTO(any(Integer.class));
		verify(service, times(1)).getTransferByUserSender(any(Integer.class), any());
	}

	@Test
	public void testGivenNewTransferMoney_WhenSaveTransferMoney_ThenReturnRedirectToTransfer() throws Exception {
		given(service.saveTransferMoney(any(), any(Integer.class))).willReturn(ResponseForOperation.OK);

		mockMvc
		.perform(post("/user/saveTransferMoney")
				.flashAttr("transferMoneyDTO", transferMoneyDTO))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/transfer"));
//			.andDo(print());

		verify(service, times(1)).saveTransferMoney(any(), any(Integer.class));
	}

	@Test
	public void testGivenBadNewTransferMoney_WhenSaveTransferMoney_ThenReturnRedirectToTransfer() throws Exception {
		given(service.saveTransferMoney(any(), any(Integer.class))).willReturn(ResponseForOperation.NO_BANK_ACCOUNT_OR_CREDIT_CARD);

		mockMvc
		.perform(post("/user/saveTransferMoney")
				.flashAttr("transferMoneyDTO", transferMoneyDTO))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/transfer?error"));
//			.andDo(print());

		verify(service, times(1)).saveTransferMoney(any(), any(Integer.class));
	}

}
