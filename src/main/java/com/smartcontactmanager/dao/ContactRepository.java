package com.smartcontactmanager.dao;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
	
	
	@Query(value = "select * from contacts where contact_id = ? ", nativeQuery = true)
	public Contact getContactById(@Param("id") int id);
	
	@Query(value = "update contacts where contact_id = ?", nativeQuery = true)
	public Contact updateContact(@Param("id") int id);
	
	
	

}
