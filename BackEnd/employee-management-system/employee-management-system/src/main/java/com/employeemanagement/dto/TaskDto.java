package com.employeemanagement.dto;

import lombok.Data;

@Data
public class TaskDto {

	private int id;
	
	private String name;
	
	private String description;
	
	private String assignedToManager;
	
	private String assignedToEmployee;
	
	private String projectStatus;
	
	private int projectId ;
	
	private int employeeId;
}
