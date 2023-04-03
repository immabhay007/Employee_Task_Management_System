package com.employeemanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employeemanagement.entity.Project;

@Repository
public interface ProjectDao extends JpaRepository<Project, Integer> {
	
	List<Project> findByNameContainingIgnoreCase(String projectName);
//	Project findProjectByName(String projectName);
	Project findByName(String projectName);
	// to find projects by managerId
	List<Project> findByManagerId(int managerId);
	
	
}
