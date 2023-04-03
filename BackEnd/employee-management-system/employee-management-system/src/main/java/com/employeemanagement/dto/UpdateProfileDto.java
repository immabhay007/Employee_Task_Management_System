package com.employeemanagement.dto;

import lombok.Data;

@Data
public class UpdateProfileDto {
	
    private String firstName;
	
	private String lastName;
	
	private int age;
	
	private String sex;
	
//	private String emailId;
	
	private String contact;
	
	private String street;
	
	private String city;
	
	private String pincode;
	
//	private String password;
	
	private String role;
}
