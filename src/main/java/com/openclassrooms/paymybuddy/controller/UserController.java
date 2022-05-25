package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.model.Transfer;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.RoleRepo;
import com.openclassrooms.paymybuddy.repository.TransferRepo;
import com.openclassrooms.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.table.TableRowSorter;
import javax.validation.Valid;

@Controller
public class UserController {

	@Autowired
	TransferRepo transferRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/signup")
	public String showSignUpForm(User user) {
		return "signup";
	}

	@PostMapping("/signup")
	public String addUser(User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signup";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role role = roleRepo.findByName("USER").get();
		user.setRole(role);
		userRepo.save(user);
		return "redirect:/index?success";
	}

	@GetMapping("/index")
	public String showUserList(Transfer transfer, Model model) {
		model.addAttribute("users", userRepo.findAll());
		model.addAttribute("transfers", transferRepo.findAll());
		return "transfer";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, @Valid User user,
	                         BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "update-user";
		}

		userRepo.save(user);
		return "redirect:/index";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepo.delete(user);
		return "redirect:/index";
	}

	@PostMapping("/user/transfer")
	public String doTransfer(Transfer transfer, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "index";
		}

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User sender = userRepo.findByEmail(userDetails.getUsername()).get();
		transfer.setSender(sender);
		transferRepo.save(transfer);
		return "redirect:/index?success";
	}


}
