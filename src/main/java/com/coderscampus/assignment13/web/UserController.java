package com.coderscampus.assignment13.web;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AddressService;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		Address address = new Address();
		address.setUser(user);
		address.setUserId(user.getUserId());
		user.setAddress(address);
		addressService.save(address);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		List<Address> addresses = addressService.findAll();
		model.put("users", users);
		model.put("address", addresses);
		if (users.size() == 1) {
			users.toArray();
			User user = users.iterator().next();
			model.put("user", users.iterator().next());
			if (addressService.existsById(user.getUserId())) {
				model.put("users", Arrays.asList(users));
				model.put("user",user);
				model.put("address", addressService.findById(user.getUserId()));	
			} else {
				Address address = new Address();
				address.setUser(user);
				address.setUserId(user.getUserId());
				user.setAddress(address);
				model.put("users", Arrays.asList(user));
				model.put("user", user);
				model.put("address", address);
			}	
		}
		return "users";
	}
	
	@PostMapping("/users")
	public String postUser (User user, Address address) {
		System.out.println(user);
		User existingUser = userService.findByIdWithAccounts(user.getUserId());
		user.setAccounts(existingUser.getAccounts());
		System.out.println(user);
		user.setAddress(address);
		address.setUser(user);
		address.setUserId(user.getUserId());
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}
	
	
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		Address address = new Address();
		if (user.getAddress()==null) {
			//Address address = new Address();
			address.setUser(user);
			address.setUserId(userId);
			model.put("address", address);
		}else {
			model.put("address", addressService.findById(userId));
		}

		model.put("users", Arrays.asList(user));
		model.put("user", user);
		
		return "users";
	}
	
//	@PostMapping("/users/{userId}")
//	public String postOneUser (User user) {
//		userService.saveUser(user);
//		return "redirect:/users/"+user.getUserId();
//	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser (@PathVariable Long userId, User user, Address address) {
		User existingUser = userService.findByIdWithAccounts(userId);
		user.setAccounts(existingUser.getAccounts());
		//addressService.save(user.getAddress());
		System.out.println(user);
		user.setAddress(address);
		address.setUser(user);
		address.setUserId(userId);
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
}

