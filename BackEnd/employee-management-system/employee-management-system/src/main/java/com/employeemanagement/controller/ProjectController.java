package com.employeemanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.dto.AssignProjectDto;
import com.employeemanagement.dto.CommanApiResponse;
import com.employeemanagement.dto.ProjectDto;
import com.employeemanagement.dto.UpdateProjectDto;
import com.employeemanagement.entity.Project;
import com.employeemanagement.entity.Task;
import com.employeemanagement.entity.User;
import com.employeemanagement.service.ProjectService;
import com.employeemanagement.service.TaskService;
import com.employeemanagement.service.UserService;
import com.employeemanagement.utility.Constants.ProjectAssignStatus;
import com.employeemanagement.utility.Constants.ProjectStatus;
import com.employeemanagement.utility.Constants.ResponseCode;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/project/")
@CrossOrigin()
public class ProjectController {
	
	Logger LOG = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
    private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@PostMapping("add")
	@ApiOperation(value = "Api to add project")
	public ResponseEntity<?> addProject(@RequestBody Project project) {
		

		CommanApiResponse response = new CommanApiResponse();

		project.setAssignStatus(ProjectAssignStatus.NOT_ASSIGNED.value());
		project.setStatus(ProjectStatus.NOT_ASSIGNED.value());  // not assigned to Manager
		
		Project addedProduct = this.projectService.addProject(project);
		
		if (addedProduct != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Project Added Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add project");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all projects")
	public ResponseEntity<?> fetchAllProjects() {
		LOG.info("Recieved request for Fetch projects");

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjects();
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	
        	if(project.getManagerId() == 0) {
        		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(project.getManagerId());
            	
            	if(manager == null) {
            		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            		projectDto.setProjectStatus(project.getStatus());
            	}
        	}
        	
        	if(project.getEmployeeId() == 0) {
        		projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
//        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(project.getEmployeeId());
            	
            	if(employee == null) {
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
//            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(employee.getFirstName()+" "+employee.getLastName());
            		projectDto.setProjectStatus(project.getStatus());
            	}
            	
            	projectDtos.add(projectDto);
        	}
        	
        	
        }
        
		return new ResponseEntity(projectDtos, HttpStatus.OK);

	}
	
	@GetMapping("search")
	@ApiOperation(value = "Api to fetch all projects by name")
	public ResponseEntity<?> fetchAllProjectsByName(@RequestParam("projectName") String projectName) {
		LOG.info("Recieved request for Fetch projects by name");

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByProjectName(projectName);
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	
        	if(project.getManagerId() == 0) {
        		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(project.getManagerId());
            	
            	if(manager == null) {
            		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            	}
        	}
        	
        	if(project.getEmployeeId() == 0) {
        		projectDto.setAssignedToEmployee(ProjectAssignStatus.ASSIGNED_TO_EMPLOYEE.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(project.getEmployeeId());
            	
            	if(employee == null) {
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.ASSIGNED_TO_EMPLOYEE.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(employee.getFirstName()+" "+employee.getLastName());
            		projectDto.setProjectStatus(project.getStatus());
            	}
            	
            	projectDtos.add(projectDto);
        	}
        	
        }
        
		return new ResponseEntity(projectDtos, HttpStatus.OK);

	}
	
	@GetMapping("search/id")
	public ResponseEntity<?> fetchAllProjectsByName(@RequestParam("projectId") int projectId) {

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        Project p = this.projectService.getProjectById(projectId);

        if(p != null) {
          projects.add(p);
        }
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	
        	if(project.getManagerId() == 0) {
        		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(project.getManagerId());
            	
            	if(manager == null) {
            		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            	}
        	}
        	
        	if(project.getEmployeeId() == 0) {
        		projectDto.setAssignedToEmployee(ProjectAssignStatus.ASSIGNED_TO_EMPLOYEE.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(project.getEmployeeId());
            	
            	if(employee == null) {
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.ASSIGNED_TO_EMPLOYEE.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(employee.getFirstName()+" "+employee.getLastName());
            		projectDto.setProjectStatus(project.getStatus());
            	}
            	
            	projectDtos.add(projectDto);
        	}
        	
        }
        
		return new ResponseEntity(projectDtos, HttpStatus.OK);

	}
	
	@GetMapping("fetch/{id}")
	@ApiOperation(value = "Api to fetch all projects")
	public ResponseEntity<?> fetchAllProjectsByid(@PathVariable("id") int managerId) {
		LOG.info("Recieved request for Fetch projects");

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
//        projects = this.projectService.getAllProjects();
        
        projects  = this.projectService.getAllProjectByManagerId(managerId);
        		
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	
        	if(project.getManagerId() == 0) {
        		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
        	else {
        		User manager = this.userService.getUserById(project.getManagerId());
            	
            	if(manager == null) {
            		projectDto.setAssignedToManager(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(manager.getFirstName()+" "+manager.getLastName());
            		projectDto.setProjectStatus(ProjectStatus.WORKING.value());
            	}
        	}
        	
        	if(project.getEmployeeId() == 0) {
        		projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
//        	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
        	else {
        		User employee = this.userService.getUserById(project.getEmployeeId());
            	
            	if(employee == null) {
            	    projectDto.setAssignedToEmployee(ProjectAssignStatus.NOT_ASSIGNED.value());
//            	    projectDto.setProjectStatus(ProjectStatus.NOT_ASSIGNED.value());
            	    
            	    projectDtos.add(projectDto);
            	    
            	    continue;
            	    
            	} else {
            		projectDto.setAssignedToManager(employee.getFirstName()+" "+employee.getLastName());
            		projectDto.setProjectStatus(project.getStatus());
            	}
            	
            	projectDtos.add(projectDto);
        	}
        	
        	
        }
        
		return new ResponseEntity(projectDtos, HttpStatus.OK);

	}
	
	//method to assign project to manager
	@PutMapping("assignProject")
	public ResponseEntity<?> assignProject(@RequestBody AssignProjectDto projectManager  ) {
		System.out.println(projectManager);
		
		CommanApiResponse response = new CommanApiResponse();
		String projectName = projectManager.getProjectName();
		String managerName = projectManager.getManagerName();
		
	
//		System.out.println(manager);
		Project project = projectService.getProjectByProjectName(projectName);
		
		User manager = userService.getUserByFirstName(managerName);
		System.out.println(manager);
		System.out.println(project);
	
			project.setStatus(ProjectStatus.PENDING.value());
			project.setManagerId(manager.getId());
//			updatedProject =project.addProject(project);
		
		//this method updates project details as id is not null and PutMapping has been requested
		Project updatedProject = this.projectService.addProject(project);
		
		if (updatedProject != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Project Updated Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update project");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
//		System.out.println(project);
//		return "";
	}
	
	 @GetMapping("completeProject/{id}")
	 public ResponseEntity<?> completeProject(@PathVariable("id") int id){
		 
//		 String projectName = updatedProject.getProjectName();
		 
//		 Project project = this.projectService.getProjectByProjectName(projectName);
		 
//		 int projectId = project.getId();
		 
		 
		 List<Project> projects = this.projectService.getAllProjectByManagerId(id);
		
		 List<Project> newProjectList = new ArrayList<Project>();
		 
		 for (Project project : projects) {
			 int projectId = project.getId();
			
			 List<Task> tasks = this.taskService.getTaskByProjectId(id);
			 int count = 0;
			 for (Task task : tasks) {
				 if(task.getStatus()==ProjectStatus.COMPLETED.value()) {
					 count ++;
				 }
			}
			 if(count==tasks.size()) {
				 newProjectList.add(project);
			 }
		}
		 
		 
		 
		 
//		 List<Task> tasks = this.taskService.getAllTasksByManagerId(id);
//		 
//		 int count = 0;		 
//		 for (Task task : tasks) {
//			for(int i =0;i<tasks.size();i++) {
//				
//			}
//		}
//		 if(count==tasks.size()) {
//			 
//			 return new ResponseEntity(tasks,HttpStatus.OK);
//		 }
		 if(!newProjectList.isEmpty()) {
			 return new ResponseEntity(newProjectList,HttpStatus.OK);
		 }
		 return new ResponseEntity(newProjectList,HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 
	 @PutMapping("updateProject")
	 public ResponseEntity<?> updateProject(@RequestBody UpdateProjectDto updatedProject){
//		 String projectName = updatedProject.getProjectName();
		 
		 Project project = this.projectService.getProjectByProjectName(updatedProject.getProjectName());
		 
		 project.setStatus(updatedProject.getUpdatedStatus());
		 
		 Project completedProject = this.projectService.addProject(project);
		 
		 if(completedProject!=null) { 
			 return new ResponseEntity(completedProject, HttpStatus.OK);
		 }
		 return new ResponseEntity(completedProject, HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	
}
