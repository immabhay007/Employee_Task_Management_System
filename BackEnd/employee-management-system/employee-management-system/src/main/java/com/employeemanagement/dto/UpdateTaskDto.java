package com.employeemanagement.dto;

import lombok.Data;

@Data
public class UpdateTaskDto {

	private String taskName;
	
	private String updatedStatus;
}
