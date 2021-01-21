package com.cdac.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.Login;
import com.cdac.dto.LoginStatus;
import com.cdac.dto.ProfilePicDetails;
import com.cdac.dto.RegisterStatus;
import com.cdac.dto.Status;
import com.cdac.dto.Status.StatusType;
import com.cdac.entity.Customer;
import com.cdac.exception.CustomerServiceException;
import com.cdac.service.CustomerService;
import com.cdac.service.ProfilePicService;

@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProfilePicService profilePicService;
	
	@PostMapping("/register")
	public RegisterStatus register(@RequestBody Customer customer) {
		try {
			int id = customerService.register(customer);
			RegisterStatus status = new RegisterStatus();
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Registration Successful!");
			status.setRegisteredCustomerId(id);
			return status;
		}
		catch(CustomerServiceException e) {
			RegisterStatus status = new RegisterStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return status;
		}
	}
	
	@PostMapping("/login")
	public LoginStatus login(@RequestBody Login login) {
		try {
			Customer customer = customerService.login(login);
			LoginStatus status = new LoginStatus();
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Login successful!");
			status.setCustomerId(customer.getId());
			status.setName(customer.getName());
			return status;
		}
		catch(CustomerServiceException e) {
			LoginStatus status = new LoginStatus();
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return status;
		}
	}
	
	@PostMapping("/pic-upload")
	public Status upload(ProfilePicDetails profilePicDetails) {
		profilePicService.upload(profilePicDetails);
		
		Status status = new Status();
		status.setStatus(StatusType.SUCCESS);
		status.setMessage("Upload successful!");
		return status;
	}
	
	@GetMapping("/profile")
	public Customer profile(@RequestParam("customerId") int customerId, HttpServletRequest request) {
		Customer customer = customerService.get(customerId);
		
		//reading the project's deployed folder location
		//TODO: move all this logic in ProfilePicService class
		String projPath = request.getServletContext().getRealPath("/");
		String downloadPath = projPath + "/downloads/";
		File f = new File(downloadPath);
		if(!f.exists())
			f.mkdir();
		
		//the original location where the images are present
		String uploadedPath = "/Volumes/BigSur/users/majrul/Documents/uploads/";//"d:/uploads/";
		String uploadedFileName = customer.getProfilePic();
		
		String sourceFile = uploadedPath + uploadedFileName;
		String destinationFile = downloadPath + uploadedFileName;
		try {
			FileCopyUtils.copy(new File(sourceFile), new File(destinationFile));
		}
		catch(IOException e) {
			e.printStackTrace(); //rather we should throw proper exception
		}
		
		return customer;
	}
}
