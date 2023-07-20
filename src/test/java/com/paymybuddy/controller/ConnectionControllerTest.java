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
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.dto.EmailUserConnectionDTO;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ConnectionControllerTest {

	@Autowired
	private ConnectionController controller;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConnectionService service;

	private static Page<EmailUserConnectionDTO> pageEmailUserConnectionDTO;
	private static List<EmailUserConnectionDTO> listDTO;
	private static EmailUserConnectionDTO emailUserConnectionDTO1;
	private static UserConnection userConnection1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		userConnection1 = new UserConnection();
		userConnection1.setPseudo("Me");

		emailUserConnectionDTO1 = new EmailUserConnectionDTO();
		emailUserConnectionDTO1.setEmail("c@email.com");
		emailUserConnectionDTO1.setUserConnection(userConnection1);

		listDTO = new ArrayList<>();
		listDTO.add(emailUserConnectionDTO1);

		pageEmailUserConnectionDTO = new PageImpl<EmailUserConnectionDTO>(listDTO);

	}

	@Test
	public void testGivenController_ThenReturnNotNull() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void test_WhenChooseConnection_ThenReturnOk() throws Exception {
		given(service.getListEmailAndPseudoByUserSender(any(Integer.class), any())).willReturn(pageEmailUserConnectionDTO);

		mockMvc
			.perform(get("/user/transfer/connection"))
			.andExpect(status().isOk())
			.andExpect(view().name("connection"))
			.andExpect(model().attributeExists("emailUserConnectionDTO"));
//			.andDo(print());

		verify(service, times(1)).getListEmailAndPseudoByUserSender(any(Integer.class), any());
	}

	@Test
	public void testGivenNewConnection_WhenSaveConnection_ThenReturnRedirectOk() throws Exception {
		given(service.saveConnection(any(), any(Integer.class))).willReturn(ResponseForOperation.OK);

		mockMvc
		.perform(post("/user/transfer/saveConnection")
				.flashAttr("emailUserConnectionDTO", emailUserConnectionDTO1))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/transfer/connection"));
//			.andDo(print());

		verify(service, times(1)).saveConnection(any(), any(Integer.class));
	}

	@Test
	public void testGivenInvalidEmail_WhenSaveConnection_ThenReturnRedirectError() throws Exception {
		given(service.saveConnection(any(), any(Integer.class))).willReturn(ResponseForOperation.EMAIL_INVALID);

		mockMvc
		.perform(post("/user/transfer/saveConnection")
				.flashAttr("emailUserConnectionDTO", emailUserConnectionDTO1))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/transfer/connection?error"));
//			.andDo(print());

		verify(service, times(1)).saveConnection(any(), any(Integer.class));
	}

	@Test
	public void test_WhenDeleteConnection_ThenReturnRedirectOk() throws Exception {
		mockMvc
			.perform(get("/user/transfer/deleteConnection")
					.param("id", "3"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/transfer/connection"));
//			.andDo(print());
	}

}
