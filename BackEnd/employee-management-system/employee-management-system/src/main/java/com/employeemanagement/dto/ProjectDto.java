package com.employeemanagement.dto;

import lombok.Data;

@Data
public class ProjectDto {
	
	private int id;
	
	private String name;
	
	private String description;
	
	private String assignedToManager;
	
	private String assignedToEmployee;
	
	private String projectStatus;

}
