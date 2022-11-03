package com.smartcontactmanager.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;


@Controller
public class MainController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserRepository userRepo;

	@GetMapping("/")
	public String home(Model m) {

		System.out.println("Inside home method.......");

		m.addAttribute("title", "Home | Smart Contact Manager");
		return "index";
	}

	@GetMapping("/register")
	public String signUp(Model m) {

		m.addAttribute("title", "Register | Smart Contact Manager");
		m.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/processing-registration")
	public String doRegister(@Valid @ModelAttribute("user") User user, BindingResult bindResult, Model m,
			HttpSession httpSession) {
		try {

			if (bindResult.hasErrors()) {

				m.addAttribute("user", user);
				return "register";
			}

			Integer userByEmail = userRepo.getUserByEmail(user.getEmail());

			if (userByEmail == null) {

				user.setEnable(true);
				user.setRole("ROLE_USER");
				user.setProfileImageUrl("default.png");
				user.setPassword(passwordEncoder.encode(user.getPassword()));

				this.userRepo.save(user);
				

				m.addAttribute("user", new User());
				m.addAttribute("status", "success");
				return "register";

			} else {
				m.addAttribute("user", user);
				m.addAttribute("status", "email-exist");
				return "register";
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return "register";
	}
	
	@GetMapping("/login")
	public String login(Model m) {
		
		m.addAttribute("title", "Login | Smart Contact Manager");
		return "login";
		
	}
	

}
