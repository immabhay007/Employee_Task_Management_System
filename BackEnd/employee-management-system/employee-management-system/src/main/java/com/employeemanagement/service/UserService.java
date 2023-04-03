package com.employeemanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeemanagement.dao.UserDao;
import com.employeemanagement.entity.User;


@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public User registerUser(User user) {
		User registeredUser = null;
		if(user != null) {
			registeredUser = this.userDao.save(user);
		}
		
		return registeredUser;
	}
	
	public User getUserByEmailIdAndPassword(String emailId, String password) {
		return this.userDao.findByEmailIdAndPassword(emailId, password);
	}
	
	public User getUserByEmailIdAndPasswordAndRole(String emailId, String password, String role) {
		return this.userDao.findByEmailIdAndPasswordAndRole(emailId, password, role);
	}
	
	public User getUserByEmailIdAndRole(String emailId, String role) {
		return this.userDao.findByEmailIdAndRole(emailId, role);
	}
	
	public User getUserByEmailId(String emailId) {
		return this.userDao.findByEmailId(emailId);
	}
	
	public List<User> getUsersByRole(String role) {
		return this.userDao.findByRole(role);
	}
	
	public User getUserById(int userId) {
		return this.userDao.findById(userId).get();
	}

	public User getUserByFirstName(String managerName) {
		
		return userDao.findByFirstName(managerName);
	}

	public User updateUser(User user) {
		
		return this.userDao.save(user);
	}

	public User getUserByfirstNameAndLastName(String firstName, String lastName) {
		
		return this.getUserByfirstNameAndLastName(firstName, lastName);
	}

	public User getUserByfirstEmail(String email) {
		
		return this.userDao.findByEmailId(email);
	}

	
	
}
