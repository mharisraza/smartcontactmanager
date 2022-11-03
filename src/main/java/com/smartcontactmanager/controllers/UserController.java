package com.smartcontactmanager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;


@Controller
@RequestMapping("/user")
public class UserController {

	/* Random String generator for image to avoid same names in database */

	static String usingRandomUUID() {

		UUID randomUUID = UUID.randomUUID();

		return randomUUID.toString().replaceAll("_", "");

	}

	/* END ************/

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactRepository contactRepo;

	@ModelAttribute
	public void commonData(Model m, Principal principal) {

		String userEmail = principal.getName();

		User user = this.userRepo.loadUserByUserName(userEmail);

		m.addAttribute("user", user);
	}

	@GetMapping("/home")
	public String userDashboard(Model m) {

		m.addAttribute("title", "Home | Smart Contact Manager");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String addContactForm(Model m) {

		m.addAttribute("title", "Add Contact | Smart Contact Manager");
		m.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

	/* Processing Contact Form */

	@PostMapping("/process-contact")
	public String processingAddContactForm(@Valid @ModelAttribute Contact contact, BindingResult bindResult, Model m,
			@RequestParam("profile") MultipartFile file, Principal principal, HttpSession httpSession) {
		try {

			if (bindResult.hasErrors()) {

				m.addAttribute("contact", contact);
				return "normal/add_contact";
			}

			String generatedSting = usingRandomUUID();

			String fileName = generatedSting + "_" + file.getOriginalFilename();

			if (file.isEmpty()) {

				contact.setProfileImage("contact.png");
			}

			else {
				contact.setProfileImage(fileName);

				File saveFile = new ClassPathResource("static/contact_images").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			String userEmail = principal.getName();
			User user = this.userRepo.loadUserByUserName(userEmail);

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepo.save(user);

			m.addAttribute("status", "success");

		} catch (Exception e) {
			e.printStackTrace();

			m.addAttribute("status", "failed");
		}

		return "normal/add_contact";
	}

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {

		String userName = principal.getName();
		User user = this.userRepo.loadUserByUserName(userName);

		Pageable pageRequest = (Pageable) PageRequest.of(page, 5);

		Page<Contact> contactsList = this.contactRepo.findContactsByUser(user.getId(), pageRequest);

		m.addAttribute("contacts", contactsList);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contactsList.getTotalPages());

		m.addAttribute("title", "Contacts | Smart Contact Manager");
		return "normal/show_contacts";
	}

	@GetMapping("/contact/{id}")
	public String showParticularContact(@PathVariable("id") Integer id, Model m, Principal principal,
			HttpSession httpSession) {

		Contact contact = this.contactRepo.getContactById(id);

		if (contact == null) {

			httpSession.setAttribute("status", "not-exist");
			return "redirect:/user/show-contacts/0";
		}

		User user = this.userRepo.loadUserByUserName(principal.getName());

		if (user.getId() == contact.getUser().getId()) {

			m.addAttribute("contact", contact);
			m.addAttribute("title", contact.getName() + " | Smart Contact Manager");

		}
		return "normal/contact_detail";
	}

	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") int id, Model m, Principal principal, HttpSession httpSession) {

		Contact contact = this.contactRepo.getContactById(id);
		User user = this.userRepo.loadUserByUserName(principal.getName());

		if (contact == null) {

			httpSession.setAttribute("status", "not-exist");
			return "redirect:/user/show-contacts/0";
		}

		if (user.getId() == contact.getUser().getId()) {

			try {

				this.contactRepo.delete(contact);
				httpSession.setAttribute("status", "delete-success");

				File saveFile = new ClassPathResource("static/contact_images/" + contact.getProfileImage()).getFile();

				if (!saveFile.getName().equals("contact.png")) {

					saveFile.delete();
				}

				return "redirect:/user/show-contacts/0";

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		httpSession.setAttribute("status", "delete-error");
		return "normal/show_contacts";

	}

	@PostMapping("/update-contact/{id}")
	public String updateContact(@PathVariable("id") int id, Model m, Principal principal) {

		Contact contact = this.contactRepo.findById(id).get();

		m.addAttribute("contact", contact);
		return "normal/update_contact";
	}

	@PostMapping("/update-contact")
	public String processUpdatedContact(@ModelAttribute Contact contact, Model m,
			@RequestParam("profile") MultipartFile file, Principal principal) {

		try {

			Contact oldContact = this.contactRepo.findById(contact.getId()).get();

			if (!file.isEmpty()) {
				
				String fileName = usingRandomUUID() + "_" +file.getOriginalFilename();

				File saveFile = new ClassPathResource("static/contact_images").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setProfileImage(fileName);

			} else {
				contact.setProfileImage(oldContact.getProfileImage());
			}

			User user = this.userRepo.loadUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/show-contacts/0";
	}

}
