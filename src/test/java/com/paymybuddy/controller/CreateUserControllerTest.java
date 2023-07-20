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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.service.CreateUserService;
import com.paymybuddy.service.dto.NewUserDTO;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CreateUserControllerTest {

	@Autowired
	private CreateUserController controller;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	public CreateUserService service;

	@Test
	public void testGivenController_ThenReturnNotNull() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testGivenGetCreateUser_ThenReturnOk() throws Exception {
		mockMvc
			.perform(get("/newUser/createUser"))
			.andExpect(status().isOk())
			.andExpect(view().name("createUser"))
			.andExpect(model().attributeExists("newUserDTO", "profileSaved"));
	}

	@Test
	public void testGivenNewUser_WhenSaveNewUser_ThenReturnOk() throws Exception {
		given(service.saveNewUser(any())).willReturn(ResponseForOperation.OK);

		NewUserDTO newUserDTO = new NewUserDTO();
		newUserDTO.setEmail("q@email.com");
		newUserDTO.setFirstName("Firstname");
		newUserDTO.setLastName("Lastname");
		newUserDTO.setPassword("123456");
		newUserDTO.setMatchingPassword("123456");

		mockMvc
		.perform(post("/newUser/saveNewUser")
				.flashAttr("newUserDTO", newUserDTO))
			.andExpect(status().isOk())
			.andExpect(view().name("createUser"))
			.andExpect(model().attributeExists("newUserDTO"));
//			.andDo(print());

		verify(service, times(1)).saveNewUser(any());
	}

	@Test
	public void testGivenBadNewUser_WhenSaveNewUser_ThenReturnEMAIL_MUST_BE_CHANGED() throws Exception {
		given(service.saveNewUser(any())).willReturn(ResponseForOperation.EMAIL_MUST_BE_CHANGED);

		NewUserDTO newUserDTO = new NewUserDTO();
		newUserDTO.setEmail("q@email.com");
		newUserDTO.setFirstName("Firstname");
		newUserDTO.setLastName("Lastname");
		newUserDTO.setPassword("123456");
		newUserDTO.setMatchingPassword("123456");

		mockMvc
		.perform(post("/newUser/saveNewUser")
				.flashAttr("newUserDTO", newUserDTO))
			.andExpect(status().isOk())
			.andExpect(view().name("createUser"))
			.andExpect(model().attributeExists("newUserDTO"));
//			.andDo(print());

		verify(service, times(1)).saveNewUser(any());
	}

}
