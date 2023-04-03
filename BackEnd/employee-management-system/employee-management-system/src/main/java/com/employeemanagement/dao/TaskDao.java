package com.employeemanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.entity.Task;

public interface TaskDao extends JpaRepository<Task, Integer> {

	List<Task> findByNameContainingIgnoreCase(String taskName);
	
	Task findByName(String taskName);
	
	List<Task> findByManagerId(int managerId);
	
	//to get all tasks related to a employee
	List<Task> findByEmployeeId(int emplpyeeId);
	
	//to get All the tasks of particular project
	List<Task> findByProjectId(int projectId);
}
