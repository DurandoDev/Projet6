package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ConnectionsRepo;
import com.openclassrooms.paymybuddy.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;Fic
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ConnectionsRepo connectionsRepo;

	@BeforeEach
	public void Init(){
		User u = new User();
		Role role =new Role();
		u.setFirstName("test");
		u.setLastName("testName");
		u.setEmail("toto@gmail.com");
		u.setPassword("testPassword");
		u.setRole(role);

		userRepo.save(u);
	}

	@AfterEach
	public void Clean(){
		connectionsRepo.deleteAll();
		userRepo.deleteAll();
	}

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
	public void testShowAddConnection() throws Exception {
		mockMvc.perform(get("/user/addconnection"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testShowProfile() throws Exception {
		mockMvc.perform(get("/user/profile"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testDeleteUser() throws Exception {

		User user = new User();

		user.setFirstName("testController");
		user.setLastName("userName");

		userRepo.save(user);

		mockMvc.perform(get("/delete/"+user.getId()))
				.andExpect(redirectedUrl("/transfer")).andExpect(status().isFound());
	}

	@Test
	public void testPostSignup() throws Exception {
		mockMvc.perform(post("/signup")
						.param("firstName","testFirstName")
						.param("lastName","testLastName")
						.param("email","testemail@email.com")
						.param("password","testPassword"))
				.andExpect(redirectedUrl("/login"))
				.andExpect(status().isFound());
	}

	@Test
	@WithMockUser(username = "toto@gmail.com", password = "abc123", roles = "USER")
	public void testAddConnection() throws Exception {

		User user = new User();

		user.setFirstName("testController");
		user.setLastName("userName");
		user.setEmail("testemail@email.com");

		userRepo.save(user);
		mockMvc.perform(post("/user/addconnection")
						.param("email","testemail@email.com"))
				.andExpect(redirectedUrl("/transfer?success"))
				.andExpect(status().isFound());
	}
}
