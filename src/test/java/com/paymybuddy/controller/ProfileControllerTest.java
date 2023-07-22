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

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ProfileService;
import com.paymybuddy.service.dto.ProfileDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class ProfileControllerTest.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTest {

	/** The controller. */
	@Autowired
	private ProfileController controller;

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/** The service. */
	@MockBean
	public ProfileService service;

	/** The profile DTO. */
	private static ProfileDTO profileDTO;
	
	/** The external bank account. */
	private static ExternalBankAccount externalBankAccount;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		profileDTO = new ProfileDTO();
		profileDTO.setEmail("c@email.com");
		profileDTO.setFirstName("Firstname");
		profileDTO.setLastName("Lastname");

		externalBankAccount = new ExternalBankAccount();
		externalBankAccount.setUser(new User());
		externalBankAccount.setBankAccount(null);
		externalBankAccount.setCreditCard(null);

	}

	/**
	 * Test given controller then return not null.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenController_ThenReturnNotNull() throws Exception {
		assertThat(controller).isNotNull();
	}

	/**
	 * Test given get user profile then return ok.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenGetUserProfile_ThenReturnOk() throws Exception {
		given(service.getProfileByUserId(any(Integer.class))).willReturn(Optional.of(profileDTO));
		given(service.getExternalBankAccountByUserId(any(Integer.class))).willReturn(Optional.of(externalBankAccount));

		mockMvc
			.perform(get("/user/profile"))
			.andExpect(status().isOk())
			.andExpect(view().name("profile"))
			.andExpect(model().attributeExists("profileDTO"));

		verify(service, times(1)).getProfileByUserId(any(Integer.class));
		verify(service, times(1)).getExternalBankAccountByUserId(any(Integer.class));
	}

	/**
	 * Test given new user profile when save new user then return redirect to profile.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenNewUserProfile_WhenSaveNewUser_ThenReturnRedirectToProfile() throws Exception {
		mockMvc
		.perform(post("/user/saveProfile")
				.flashAttr("profileDTO", profileDTO))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/profile"));
//			.andDo(print());
	}

	/**
	 * Test given new bank account when save bank account then return redirect to profile.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenNewBankAccount_WhenSaveBankAccount_ThenReturnRedirectToProfile() throws Exception {
		mockMvc
		.perform(post("/user/saveBankAccount")
				.flashAttr("externalBankAccount", externalBankAccount))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/profile"));
//			.andDo(print());
	}

	/**
	 * Test given new credit card when save credit card then return redirect to profile.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenNewCreditCard_WhenSaveCreditCard_ThenReturnRedirectToProfile() throws Exception {
		mockMvc
		.perform(post("/user/saveCreditCard")
				.flashAttr("externalBankAccount", externalBankAccount))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/profile"));
//			.andDo(print());
	}

	/**
	 * Test when delete profile then return redirect to login.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test_WhenDeleteProfile_ThenReturnRedirectToLogin() throws Exception {
		mockMvc
			.perform(get("/user/deleteProfile"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/login"));
//			.andDo(print());
	}

	/**
	 * Test when delete bank account then return redirect to profile.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test_WhenDeleteBankAccount_ThenReturnRedirectToProfile() throws Exception {
		mockMvc
			.perform(get("/user/deleteBankAccount")
					.param("id", "3"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/profile"));
//			.andDo(print());
	}

	/**
	 * Test when delete credit card then return redirect to profile.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test_WhenDeleteCreditCard_ThenReturnRedirectToProfile() throws Exception {
		mockMvc
			.perform(get("/user/deleteCreditCard")
					.param("id", "3"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/profile"));
//			.andDo(print());
	}

}
