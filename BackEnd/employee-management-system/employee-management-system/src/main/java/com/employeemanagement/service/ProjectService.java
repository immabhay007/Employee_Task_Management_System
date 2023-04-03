package com.employeemanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeemanagement.dao.ProjectDao;
import com.employeemanagement.entity.Project;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
	public Project addProject(Project project) {
		return projectDao.save(project);
	}
	
	public List<Project> getAllProjects() {
		return projectDao.findAll();
	}
	
	public Project getProjectById(int projectId) {
		
		Project p = null;
		
		Optional<Project> oP = projectDao.findById(projectId);
		
		if(oP.isPresent()) {
			p = oP.get();
		}
		
		return p;
	}
	
	public List<Project> getAllProjectsByProjectName(String projectName) {
		return projectDao.findByNameContainingIgnoreCase(projectName);
	}

	public Project getProjectByProjectName(String ProjectName) {
		return projectDao.findByName(ProjectName); 				
	}

	public List<Project> getAllProjectByManagerId(int managerId) {
		
		return projectDao.findByManagerId(managerId);
	}
	
}
