package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.Connections;
import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.model.Transfer;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ConnectionsRepo;
import com.openclassrooms.paymybuddy.repository.RoleRepo;
import com.openclassrooms.paymybuddy.repository.TransferRepo;
import com.openclassrooms.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	ConnectionsRepo connectionsRepo;

	@GetMapping("/signup")
	public String showSignUpForm(User user) {return "signup";}

	@PostMapping("/signup")
	public String addUser(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return "signup";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role role = roleRepo.findByName("USER").orElse(new Role());
		user.setRole(role);
		userRepo.save(user);
		return "redirect:/login";
	}

	@GetMapping("/transfer")
	public String showUserList(Transfer transfer, Model model,
	                           @RequestParam("page") Optional<Integer> page,
	                           @RequestParam("size") Optional<Integer> size) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User connectedUser = userRepo.findByEmail(userDetails.getUsername()).get();
		model.addAttribute("connections", connectionsRepo.findByOwner_id(connectedUser.getId()));


		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Pageable pageable = PageRequest.of(currentPage-1, pageSize);

		Page<Transfer> transferPage = transferRepo.findBySender(connectedUser,pageable);

		List < Transfer > listTransferts = transferPage.getContent();

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", transferPage.getTotalPages());
		model.addAttribute("totalItems", transferPage.getTotalElements());
		model.addAttribute("transfers", listTransferts);
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
		return "redirect:/transfer";
	}

	@PostMapping("/user/transfer")
	public String doTransfer(Transfer transfer, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "index";
		}

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User sender = userRepo.findByEmail(userDetails.getUsername()).get();
		transfer.setSender(sender);
		transfer.setAmount(transfer.getAmount() - 0.005 * transfer.getAmount());
		transferRepo.save(transfer);
		return "redirect:/transfer?success";
	}

	@GetMapping("/user/addconnection")
	public String showAddConnection() {
		return "/addconnection";
	}

	@GetMapping("/user/profile")
	public String showProfile(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User owner = userRepo.findByEmail(userDetails.getUsername()).get();
		model.addAttribute("currentUser",owner);

		return "/profile";
	}

	@PostMapping("/user/addconnection")
	public String addConnection(@RequestParam String email, Model model){

		User user = userRepo.findByEmail(email).get();

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User owner = userRepo.findByEmail(userDetails.getUsername()).get();

		Connections c = new Connections(user, owner);

		connectionsRepo.save(c);

		return "redirect:/transfer?success";

	}

}