package com.employeemanagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.dto.CommanApiResponse;
import com.employeemanagement.dto.UpdateProfileDto;
import com.employeemanagement.dto.UserLoginRequest;
import com.employeemanagement.dto.UserLoginResponse;
import com.employeemanagement.dto.UserRoleResponse;
import com.employeemanagement.entity.User;
import com.employeemanagement.service.CustomUserDetailsService;
import com.employeemanagement.service.UserService;
import com.employeemanagement.utility.Constants.ResponseCode;
import com.employeemanagement.utility.Constants.Sex;
import com.employeemanagement.utility.Constants.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.employeemanagement.utility.JwtUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/user/")
@CrossOrigin()
public class UserController {

	Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("gender")
	public ResponseEntity<?> getAllUserGender() {
		
		UserRoleResponse response = new UserRoleResponse();
		List<String> genders = new ArrayList<>();
		
		for(Sex gender : Sex.values() ) {
			genders.add(gender.value());
		}
		
		if(genders.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Genders");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		else {
			response.setGenders(genders);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("User Genders Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}
		
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<User> changepassword(@RequestBody User user){
		User user1 = userService.getUserByEmailId(user.getEmailId());
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
//		User user2 = userService.registerUser(user);
		
		CommanApiResponse response = new CommanApiResponse();
		
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage(user.getRole() + " Registered Successfully");
		return new ResponseEntity(response, HttpStatus.OK);
		//return new ResponseEntity(useLoginResponse, HttpStatus.OK);  
	}
	
	@PostMapping("admin/register")
	public ResponseEntity<?> adminRegister(@RequestBody User user) {
		

		CommanApiResponse response = new CommanApiResponse();
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage(user.getRole() + " Admin Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register " + user.getRole() + " User");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("manager/register")
	public ResponseEntity<?> mangaerRegister(@RequestBody User user) {
		
		System.out.println(user);
//		LOG.info(user.toString());
		CommanApiResponse response = new CommanApiResponse();
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage(user.getRole() + " Manager Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register " + user.getRole() + " User");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("employee/register")
	public ResponseEntity<?> employeeRegister(@RequestBody User user) {
	
		
		CommanApiResponse response = new CommanApiResponse();
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage( "Employee Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register " + user.getRole() + " User");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
		

		String jwtToken = null;
		UserLoginResponse useLoginResponse = new UserLoginResponse();
        User user = null;
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userLoginRequest.getEmailId(), userLoginRequest.getPassword()));
		} catch (Exception ex) {
			LOG.error("Authentication Failed!!!");
			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(userLoginRequest.getEmailId());

		for (GrantedAuthority grantedAuthory : userDetails.getAuthorities()) {
			if (grantedAuthory.getAuthority().equals(userLoginRequest.getRole())) {
				jwtToken = jwtUtil.generateToken(userDetails.getUsername());
			}
		}

		// user is authenticated
		if (jwtToken != null) {

			user = userService.getUserByEmailId(userLoginRequest.getEmailId());
			
			useLoginResponse = User.toUserLoginResponse(user);
			
			useLoginResponse.setResponseCode(ResponseCode.SUCCESS.value());
			useLoginResponse.setResponseMessage(user.getFirstName() + " logged in Successful");
			useLoginResponse.setJwtToken(jwtToken);
			return new ResponseEntity(useLoginResponse, HttpStatus.OK);
		
		}

		else {

			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("manager/all")
	public ResponseEntity<?> getAllManager() {
		System.out.println("recieved request for getting ALL Managers!!!");
		
		List<User> managers = this.userService.getUsersByRole(UserRole.MANAGER.value());
		
		System.out.println("response sent!!!");
		return ResponseEntity.ok(managers);
	}
	
	@GetMapping("employee/all")
	public ResponseEntity<?> getAllEmployee() {
		System.out.println("recieved request for getting ALL Employees!!!");
		
		List<User> managers = this.userService.getUsersByRole(UserRole.EMPLOYEE.value());
		
		System.out.println("response sent!!!");
		return ResponseEntity.ok(managers);
	}
	
	@PostMapping("update/{id}")
	public ResponseEntity<?> userUpdateProfile(@PathVariable("id") int id ,@RequestBody UpdateProfileDto updatedUser) {
		System.out.println("admin update profile called");
		
		
		User user = this.userService.getUserById(id);
		
		user.setFirstName(updatedUser.getFirstName());
		user.setLastName(updatedUser.getLastName());
		user.setAge(updatedUser.getAge());
		user.setCity(updatedUser.getCity());
		user.setContact(updatedUser.getContact());
		//user.setEmailId(updatedUser.getEmailId());
		user.setPincode(updatedUser.getPincode());
		user.setRole(updatedUser.getRole());
		user.setSex(updatedUser.getSex());
		user.setStreet(updatedUser.getStreet());
		
		CommanApiResponse response = new CommanApiResponse();
	//	String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());

		//user.setPassword(encodedPassword);

		User registerUser = userService.updateUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Profile updated Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update profile");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@PostMapping("manager/update/{id}")
//	public ResponseEntity<?> managerUpdateProfile(@PathVariable("id") int id ,@RequestBody UpdateProfileDto updatedUser) {
//		System.out.println("manager update profile called");
//		
//		
//		User user = this.userService.getUserById(id);
//		
//		user.setFirstName(updatedUser.getFirstName());
//		user.setLastName(updatedUser.getLastName());
//		user.setAge(updatedUser.getAge());
//		user.setCity(updatedUser.getCity());
//		user.setContact(updatedUser.getContact());
//		//user.setEmailId(updatedUser.getEmailId());
//		user.setPincode(updatedUser.getPincode());
//		user.setRole(updatedUser.getRole());
//		user.setSex(updatedUser.getSex());
//		user.setStreet(updatedUser.getStreet());
//		
//		CommanApiResponse response = new CommanApiResponse();
//	//	String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
//
//		//user.setPassword(encodedPassword);
//
//		User registerUser = userService.updateUser(user);
//
//		if (registerUser != null) {
//			response.setResponseCode(ResponseCode.SUCCESS.value());
//			response.setResponseMessage("Profile updated Successfully");
//			return new ResponseEntity(response, HttpStatus.OK);
//		}
//
//		else {
//			response.setResponseCode(ResponseCode.FAILED.value());
//			response.setResponseMessage("Failed to update profile");
//			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@PostMapping("admin/update/{id}")
//	public ResponseEntity<?> adminUpdateProfile(@PathVariable("id") int id ,@RequestBody UpdateProfileDto updatedUser) {
//		System.out.println("admin update profile called");
//		
//		
//		User user = this.userService.getUserById(id);
//		
//		user.setFirstName(updatedUser.getFirstName());
//		user.setLastName(updatedUser.getLastName());
//		user.setAge(updatedUser.getAge());
//		user.setCity(updatedUser.getCity());
//		user.setContact(updatedUser.getContact());
//		//user.setEmailId(updatedUser.getEmailId());
//		user.setPincode(updatedUser.getPincode());
//		user.setRole(updatedUser.getRole());
//		user.setSex(updatedUser.getSex());
//		user.setStreet(updatedUser.getStreet());
//		
//		CommanApiResponse response = new CommanApiResponse();
//	//	String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
//
//		//user.setPassword(encodedPassword);
//
//		User registerUser = userService.updateUser(user);
//
//		if (registerUser != null) {
//			response.setResponseCode(ResponseCode.SUCCESS.value());
//			response.setResponseMessage("Profile updated Successfully");
//			return new ResponseEntity(response, HttpStatus.OK);
//		}
//
//		else {
//			response.setResponseCode(ResponseCode.FAILED.value());
//			response.setResponseMessage("Failed to update profile");
//			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@PutMapping("manager/update")
//	public ResponseEntity<?> mangaerUpdateProfile(@RequestBody User user) {
//		
//		System.out.println(user);
////		LOG.info(user.toString());
//		CommanApiResponse response = new CommanApiResponse();
//		String encodedPassword = passwordEncoder.encode(user.getPassword());
//
//		user.setPassword(encodedPassword);
//
//		User registerUser = userService.updateUser(user);
//
//		if (registerUser != null) {
//			response.setResponseCode(ResponseCode.SUCCESS.value());
//			response.setResponseMessage("Profile updated Successfully");
//			return new ResponseEntity(response, HttpStatus.OK);
//		}
//
//		else {
//			response.setResponseCode(ResponseCode.FAILED.value());
//			response.setResponseMessage("Failed to update profile");
//			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@PutMapping("manager/update")
//	public ResponseEntity<?> mangaerUpdateProfile(@RequestBody User user) {
//		
//		System.out.println(user);
////		LOG.info(user.toString());
//		CommanApiResponse response = new CommanApiResponse();
//		String encodedPassword = passwordEncoder.encode(user.getPassword());
//
//		user.setPassword(encodedPassword);
//
//		User registerUser = userService.updateUser(user);
//
//		if (registerUser != null) {
//			response.setResponseCode(ResponseCode.SUCCESS.value());
//			response.setResponseMessage("Profile updated Successfully");
//			return new ResponseEntity(response, HttpStatus.OK);
//		}
//
//		else {
//			response.setResponseCode(ResponseCode.FAILED.value());
//			response.setResponseMessage("Failed to update profile");
//			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
}
