package com.employeemanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeemanagement.dao.TaskDao;
import com.employeemanagement.entity.Project;
import com.employeemanagement.entity.Task;

@Service
public class TaskService {

	@Autowired
	private  TaskDao taskDao;
	
	public Task addTask(Task task) {
		return taskDao.save(task);
	}
	
	public List<Task> getAllTasks() {
		return taskDao.findAll();
	}
	
	public Task getProjectById(int taskId) {
		
		Task t = null;
		
		Optional<Task> oP = taskDao.findById(taskId);
		
		if(oP.isPresent()) {
			t = oP.get();
		}
		
		return t;
	}
	
	public List<Task> getAllProjectsByTaskName(String taskName) {
		return taskDao.findByNameContainingIgnoreCase(taskName);
	}

	public Task getTaskByTaskName(String taskName) {
		
		return taskDao.findByName(taskName);
	}

	public List<Task> getAllTasksByManagerId(int managerId) {
		
		return taskDao.findByManagerId(managerId);
	}

	public List<Task> getAllTasksByEmployeeId(int employeeId) {
		
		return taskDao.findByEmployeeId(employeeId);
	}

	public List<Task> getTaskByProjectId(int projectId) {
		
		return taskDao.findByProjectId(projectId);
	}

//	public List<Task> getAllTasks() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
