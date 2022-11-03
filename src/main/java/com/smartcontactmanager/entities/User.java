package com.smartcontactmanager.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private int id;
	@NotBlank(message = "Name field is required.")
	@Column(name="user_name")
	private String name;
	@Column(unique=true, name="user_email")
	@NotBlank(message = "Email field is required.")
	private String email;
	@Column(name="user_pwd")
	@NotBlank(message = "Password field is required.")
	private String password;
	@Column(name="user_about")
	@NotBlank(message = "About field is required.")
	private String about;
	@Column(name="user_role")
	private String role;
	@Column(name="user_profileImageUrl")
	private String profileImageUrl;
	@Column(name="user_status")
	private boolean isEnable;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
	
	/* Default Constructor */
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/* Getters And Setters */

	public int getId() {
		return id;
	}

	

	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getProfileImageUrl() {
		return profileImageUrl;
	}


	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}


	public boolean isEnable() {
		return isEnable;
	}


	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}


	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", about=" + about
				+ ", role=" + role + ", profileImageUrl=" + profileImageUrl + ", isEnable=" + isEnable + ", contacts="
				+ contacts + "]";
	}
	
	
	
	
	
	
	
	

}
