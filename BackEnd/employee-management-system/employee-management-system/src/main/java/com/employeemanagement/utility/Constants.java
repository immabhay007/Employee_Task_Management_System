package com.employeemanagement.utility;

public class Constants {
	
	public enum UserRole {
		ADMIN("admin"),
		EMPLOYEE("employee"),
		MANAGER("manager");
		
		private String role;

	    private UserRole(String role) {
	      this.role = role;
	    }

	    public String value() {
	      return this.role;
	    }    
	}
	
	public enum Sex {
		MALE("Male"),
		FEMALE("Female");
		
		
		private String sex;

	    private Sex(String sex) {
	      this.sex = sex;
	    }

	    public String value() {
	      return this.sex;
	    }    
	}
	
	public enum ProjectStatus {
		COMPLETED("Completed"),
		PENDING("Pending"),
		WORKING("Working"),
		NOT_ASSIGNED("Not Assigned");
		
		private String status;

	    private ProjectStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum ProjectAssignStatus {
		ASSIGNED_TO_MANAGER("Assigned to Manager"),
		ASSIGNED_TO_EMPLOYEE("Assigned to Employee"),
		NOT_ASSIGNED("Not Assigned");
		
		
		private String status;

	    private ProjectAssignStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum ResponseCode {
		SUCCESS(0),
		FAILED(1);
		
		
		private int code;

	    private ResponseCode(int code) {
	      this.code = code;
	    }

	    public int value() {
	      return this.code;
	    }    
	}
	
}
