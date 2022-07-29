package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.configuration.SpringSecurityConfig;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;



	//Test unitaires

	@Test
	public void testShowSignUpForm() throws Exception {
		mockMvc.perform(get("/signup"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testShowUserList() throws Exception {
		mockMvc.perform(get("/transfer"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testDeleteUser() throws Exception {
		mockMvc.perform(get("/delete/{id}")
						.param("10"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testPostSignup() throws Exception {
		mockMvc.perform(post("/signup"))
				.andExpect(status().isOk());
	}
}
