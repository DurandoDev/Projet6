package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.*;
import com.openclassrooms.paymybuddy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

	@Autowired
	TransferRepo transferRepo;

	@Autowired
	BalanceRepo balanceRepo;

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
		Role role = roleRepo.findByName("USER").orElse(new Role("USER"));
		user.setRole(role);
		user.setBalance(new Balance());
		userRepo.save(user);
		return "redirect:/login";
	}

	@GetMapping("/transfer")
	public String showUserList(Transfer transfer, Model model,
	                           @RequestParam("page") Optional<Integer> page,
	                           @RequestParam("size") Optional<Integer> size) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User connectedUser = userRepo.findByEmail(userDetails.getUsername()).get();

		model.addAttribute("currentUser",connectedUser);
		model.addAttribute("connections", connectionsRepo.findByOwner_id(connectedUser.getId()));
		model.addAttribute("transfer", transfer);


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
	public String doTransfer(@RequestParam User receiver, Transfer transfer, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "index";
		}



		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User sender = userRepo.findByEmail(userDetails.getUsername()).get();
		transfer.setSender(sender);
		transfer.setReceiver(receiver);


		Balance balanceSender = sender.getBalance();
		Balance balanceReceiver = receiver.getBalance();
		balanceSender.setAmount(balanceSender.getAmount() - transfer.getAmount());
		balanceReceiver.setAmount(balanceReceiver.getAmount() + (transfer.getAmount() - 0.005 * transfer.getAmount()));
		transfer.setAmount(transfer.getAmount() - 0.005 * transfer.getAmount());

		transferRepo.save(transfer);
		balanceRepo.save(balanceSender);
		balanceRepo.save(balanceReceiver);

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

	@PostMapping("/balance")
	public String addBalance(@RequestBody MultiValueMap<String,String> body, BindingResult result){
		if (result.hasErrors()) {
			return "/user/profile";
		}

		int amount = Integer.valueOf(body.get("amount").get(0));

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User owner = userRepo.findByEmail(userDetails.getUsername()).get();
		Balance balance = owner.getBalance();
		balance.setAmount(balance.getAmount() + amount);
		balanceRepo.save(balance);
		return "redirect:/user/profile?success";
	}
}