package com.employeemanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employeemanagement.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByEmailIdAndPassword(String emailId, String password); 
	User findByEmailIdAndPasswordAndRole(String emailId, String password, String role); 
	User findByEmailIdAndRole(String emailId, String role);
	User findByEmailId(String emailId);
	List<User> findByRole(String role);
	User findByFirstName(String firstName);
	//to get user by first Name and last name 
	User findByFirstNameAndLastName(String firstName , String lastName);
}
