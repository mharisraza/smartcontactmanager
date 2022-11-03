package com.smartcontactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value = "select * from users where user_email = ?", nativeQuery = true)
	public Integer getUserByEmail(@Param("email") String email);
	
	@Query("select u from User u where u.email =:email")
	public User loadUserByUserName(@Param("email") String email);

}
