package com.employeemanagement.dto;

import lombok.Data;

@Data
public class AddTaskDto {

	private String name ;
	
	private String description ; 
	
	private String projectName ; 
	
	private int managerId;
}
