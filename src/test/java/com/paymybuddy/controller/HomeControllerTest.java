package com.paymybuddy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeControllerTest.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

	/** The controller. */
	@Autowired
	private HomeController controller;

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/** The repository. */
	@MockBean
	private UserRepository repository;

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
	 * Test given good role when get end point home admin then return ok.
	 *
	 * @throws Exception the exception
	 */
	@WithMockUser(roles = {"ADMIN"})
	@Test
	public void testGivenGoodRole_WhenGetEndPointHomeAdmin_ThenReturnOk() throws Exception {
		mockMvc
			.perform(get("/admin/homeAdmin"))
			.andExpect(status().isOk())
			.andExpect(view().name("homeAdmin"))
			.andExpect(model().attributeExists("commission"));
	}

	/**
	 * Test given good role when get end point home accounting then return ok.
	 *
	 * @throws Exception the exception
	 */
	@WithMockUser(roles = {"ACCOUNTING"})
	@Test
	public void testGivenGoodRole_WhenGetEndPointHomeAccounting_ThenReturnOk() throws Exception {
		mockMvc
			.perform(get("/accounting/homeAccounting"))
			.andExpect(status().isOk())
			.andExpect(view().name("homeAccounting"));
	}

	/**
	 * Test given good role when get end point home user then return ok.
	 *
	 * @throws Exception the exception
	 */
	@WithMockUser(username = "c@email.com", password = "123456", roles = {"USER"})
	@Test
	public void testGivenGoodRole_WhenGetEndPointHomeUser_ThenReturnOk() throws Exception {
		Role role;
		role = new Role();
		role.setRoleId(1);
		role.setRole("USER");

		User user1;
		user1 = new User();
		user1.setUserId(3);
		user1.setEmail("c@email.com");
		user1.getRoles().add(role);

		UserConnected.setUserConnected(user1);

		given(repository.findProfileByUserId(any(Integer.class))).willReturn("c@email.com, Firstname, Lastname");

		mockMvc
			.perform(get("/user/homeUser"))
			.andExpect(status().isOk())
			.andExpect(view().name("homeUser"))
			.andExpect(model().attributeExists("user"));

		verify(repository, times(1)).findProfileByUserId(any(Integer.class));
	}

	/**
	 * Test given bad role when get end point home admin then return forbidden.
	 *
	 * @throws Exception the exception
	 */
	@WithMockUser(roles = {"USER"})
	@Test
	public void testGivenBadRole_WhenGetEndPointHomeAdmin_ThenReturnForbidden() throws Exception {
		mockMvc
			.perform(get("/admin/homeAdmin"))
			.andExpect(status().isForbidden());
	}

}
