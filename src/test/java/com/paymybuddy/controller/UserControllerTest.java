package com.paymybuddy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.security.UserConnected;

// TODO: Auto-generated Javadoc
/**
 * The Class UserControllerTest.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	/** The controller. */
	@Autowired
	private UserController controller;

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

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
	 * Test given admin when redirect home then return redirection.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenAdmin_WhenRedirectHome_ThenReturnRedirection() throws Exception {
		Role role = new Role();
		role.setRole("ADMIN");

		User user1 = new User();
		user1.getRoles().add(role);

		UserConnected.setUserConnected(user1);

		mockMvc
			.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/admin/homeAdmin"));
//			.andDo(print());
	}

	/**
	 * Test given accounting when redirect home then return redirection.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenAccounting_WhenRedirectHome_ThenReturnRedirection() throws Exception {
		Role role = new Role();
		role.setRole("ACCOUNTING");

		User user1 = new User();
		user1.getRoles().add(role);

		UserConnected.setUserConnected(user1);

		mockMvc
			.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/accounting/homeAccounting"));
//			.andDo(print());
	}

	/**
	 * Test given user when redirect home then return redirection.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGivenUser_WhenRedirectHome_ThenReturnRedirection() throws Exception {
		Role role = new Role();
		role.setRole("USER");

		User user1 = new User();
		user1.getRoles().add(role);

		UserConnected.setUserConnected(user1);

		mockMvc
			.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/homeUser"));
//			.andDo(print());
	}

	/**
	 * Test when choose contact then return ok.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test_WhenChooseContact_ThenReturnOk() throws Exception {
		mockMvc
			.perform(get("/user/contact"))
			.andExpect(status().isOk())
			.andExpect(view().name("contact"))
			.andExpect(model().attributeExists("menu"));
//			.andDo(print());
	}

	/**
	 * Test when choose logout then return redirection.
	 *
	 * @throws Exception the exception
	 */
	@WithMockUser(username = "c@email.com", password = "123456", roles = {"USER"})
	@Test
	public void test_WhenChooseLogout_ThenReturnRedirection() throws Exception {
		mockMvc
			.perform(get("/logoff"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/login?logout"));
//			.andDo(print());
	}

}
