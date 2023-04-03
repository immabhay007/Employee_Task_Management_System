package com.employeemanagement.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.dto.AddTaskDto;
import com.employeemanagement.dto.AssignProjectDto;
import com.employeemanagement.dto.AssignTaskDto;
import com.employeemanagement.dto.CommanApiResponse;
import com.employeemanagement.dto.ProjectDto;
import com.employeemanagement.dto.TaskDto;
import com.employeemanagement.dto.UpdateTaskDto;
import com.employeemanagement.dto.UserRoleResponse;
import com.employeemanagement.entity.Project;
import com.employeemanagement.entity.Task;
import com.employeemanagement.entity.User;
import com.employeemanagement.service.ProjectService;
import com.employeemanagement.service.TaskService;
import com.employeemanagement.service.UserService;
import com.employeemanagement.utility.Constants.ProjectAssignStatus;
import com.employeemanagement.utility.Constants.ProjectStatus;
import com.employeemanagement.utility.Constants.ResponseCode;
import com.employeemanagement.utility.Constants.Sex;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/task/")
@CrossOrigin()
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@PostMapping("add")
	public ResponseEntity<?> addProject(@RequestBody AddTaskDto task) {
		

//		CommanApiResponse response = new CommanApiResponse();
//		System.out.println(task);
//		task.setAssignStatus(ProjectAssignStatus.ASSIGNED_TO_EMPLOYEE.value());
//		task.setStatus(ProjectStatus.NOT_ASSIGNED.value());  // not assigned to Manager
//		
//		Task addedTask = this.taskService.addTask(task);
//		
//		if (addedTask != null) {
//			response.setResponseCode(ResponseCode.SUCCESS.value());
//			response.setResponseMessage("Task Added Successfully");
//			return new ResponseEntity(response, HttpStatus.OK);
//		}
//
//		else {
//			response.setResponseCode(ResponseCode.FAILED.value());
//			response.setResponseMessage("Failed to add task");
//			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
		
		System.out.println(task);
		String taskName = task.getName();
		String taskDescription = task.getDescription();
		String projectName = task.getProjectName();
		int managerId = task.getManagerId();
		
		Project project = this.projectService.getProjectByProjectName(projectName);
		
		int projectId = project.getId();
		
		Task newTask = new Task(); 
				
		CommanApiResponse response = new CommanApiResponse();
		
		newTask.setAssignStatus(ProjectAssignStatus.NOT_ASSIGNED.value());
		newTask.setStatus(ProjectStatus.NOT_ASSIGNED.value());  // not assigned to Manager
		newTask.setProjectId(projectId);
		newTask.setManagerId(managerId);
		newTask.setDescription(taskDescription);
		newTask.setName(taskName);
		
		Task addedTask = this.taskService.addTask(newTask);
		
		return new ResponseEntity(addedTask,HttpStatus.OK);
	}
	
	@GetMapping("fetch/{id}")
	public ResponseEntity<?> fetchAllTasks(@PathVariable("id") int managerId ) {
		System.out.println("in fetch by id of task controller");

		List<TaskDto> taskDtos = new ArrayList<>(); 
		
        List<Task> tasks = new ArrayList<>();
        
//        tasks = this.taskService.getAllTasks();
        
        tasks = this.taskService.getAllTasksByManagerId(managerId);
        System.out.println(tasks);
        
        for(Task task : tasks) {
        	TaskDto taskDto = new TaskDto();
        	taskDto.setName(task.getName());
        	taskDto.setDescription(task.getDescription());
        	taskDto.setProjectId(task.getProjectId());
        	
        	if(task.getManagerId() == 0) {
        		taskDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    taskDtos.add(taskDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(task.getManagerId());
            	
            	if(manager == null) {
            		taskDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    taskDtos.add(taskDto);
            	    
            	    continue;
            	    
            	} else {
            		taskDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            		taskDto.setProjectStatus(ProjectStatus.WORKING.value());
            	}
        	}
        	
        	if(task.getEmployeeId() == 0) {
        		taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    taskDtos.add(taskDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(task.getEmployeeId());
        		
            	
            	if(employee == null) {
            	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    taskDtos.add(taskDto);
            	    
            	    continue;
            	    
            	} else {
            		taskDto.setAssignedToEmployee(employee.getFirstName()+" "+employee.getLastName());
            		taskDto.setProjectStatus(task.getStatus());
            	}
            	
            	taskDtos.add(taskDto);
        	}
        	
        }
        
		return new ResponseEntity(taskDtos, HttpStatus.OK);

	}
	
	@GetMapping("get/{id}")
	public ResponseEntity<?> fetchAllTasksByEmployeeId(@PathVariable("id") int employeeId ) {
		System.out.println("in fetch by id of task controller");

		List<TaskDto> taskDtos = new ArrayList<>(); 
		
        List<Task> tasks = new ArrayList<>();
        
//        tasks = this.taskService.getAllTasks();
        
        tasks = this.taskService.getAllTasksByEmployeeId(employeeId);
        System.out.println(tasks);
        
        for(Task task : tasks) {
        	TaskDto taskDto = new TaskDto();
        	taskDto.setName(task.getName());
        	taskDto.setDescription(task.getDescription());
        	taskDto.setProjectId(task.getProjectId());
        	
        	if(task.getManagerId() == 0) {
        		taskDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    taskDtos.add(taskDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(task.getManagerId());
            	
            	if(manager == null) {
            		taskDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    taskDtos.add(taskDto);
            	    
            	    continue;
            	    
            	} else {
            		taskDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            		taskDto.setProjectStatus(ProjectStatus.WORKING.value());
            	}
        	}
        	
        	if(task.getEmployeeId() == 0) {
        		taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    taskDtos.add(taskDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(task.getEmployeeId());
        		
            	
            	if(employee == null) {
            	    taskDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    taskDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    taskDtos.add(taskDto);
            	    
            	    continue;
            	    
            	} else {
            		taskDto.setAssignedToEmployee(employee.getFirstName()+" "+employee.getLastName());
            		taskDto.setProjectStatus(task.getStatus());
            	}
            	
            	taskDtos.add(taskDto);
        	}
        	
        }
        
		return new ResponseEntity(taskDtos, HttpStatus.OK);

	}
	
	@GetMapping("taskStatus")
	public ResponseEntity<?> getAllProjectStatus() {
		System.out.println("in update status");
		UserRoleResponse response = new UserRoleResponse();
		List<String> projectStatuses = new ArrayList<>();
		
		for(ProjectStatus projectStatus : ProjectStatus.values() ) {
			projectStatuses.add(projectStatus.value());
		}
		
		if(projectStatuses.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Genders");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		else {
			response.setProjectStatus(projectStatuses);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Project Status Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}
		
	}
	
	@PutMapping("assignTask")
	public ResponseEntity<?> assignProject(@RequestBody AssignTaskDto taskEmployee  ) {
		System.out.println(taskEmployee);
		 
		CommanApiResponse response = new CommanApiResponse();
		String taskName = taskEmployee.getTaskName();
		String employeeName = taskEmployee.getEmployeeName();
		int managerId = taskEmployee.getId();
		
	
//		System.out.println(manager);
		Task task = taskService.getTaskByTaskName(taskName);
		
		User employee = userService.getUserByFirstName(employeeName);
		System.out.println(employee);
		System.out.println(task);
		System.out.println(managerId);
	
			task.setStatus(ProjectStatus.WORKING.value());
			task.setEmployeeId(employee.getId());
			task.setManagerId(managerId);
//			updatedProject =project.addProject(project);
		
		//this method updates project details as id is not null and PutMapping has been requested
		Task updatedTask = this.taskService.addTask(task);
		
		if (updatedTask != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Task Updated Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update task");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
//		System.out.println(project);
//		return "";
	}
	
	@PutMapping("updateTask")
	public ResponseEntity<?> updateTask(@RequestBody UpdateTaskDto updatedTask){
		
		
		
		String taskName = updatedTask.getTaskName();
		String updatedStatus = updatedTask.getUpdatedStatus();
		
		Task task = this.taskService.getTaskByTaskName(taskName);
		
		task.setStatus(updatedStatus);
		
		Task updatedtask = this.taskService.addTask(task);
		
		return new ResponseEntity(updatedtask,HttpStatus.OK);
	}
}
