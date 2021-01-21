package com.cdac.service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.cdac.dto.ProfilePicDetails;
import com.cdac.entity.Customer;
import com.cdac.repository.CustomerRepository;

@Service
@Transactional
public class ProfilePicService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public void upload(ProfilePicDetails profilePicDetails) {
		String imageUploadLocation = "/Volumes/BigSur/users/majrul/Documents/uploads/";//"d:/uploads/";
		String uploadedFileName = profilePicDetails.getProfilePic().getOriginalFilename();
		String targetFileName = profilePicDetails.getCustomerId() + "-" + uploadedFileName;
		String finalTargetLocation = imageUploadLocation + targetFileName;
		try {
			FileCopyUtils.copy(profilePicDetails.getProfilePic().getInputStream(), new FileOutputStream(finalTargetLocation));
		}
		catch(IOException e) {
			e.printStackTrace(); //rather we should throw CustomerServiceException
		}
				
		Customer customer = customerRepository.fetch(profilePicDetails.getCustomerId());
		customer.setProfilePic(targetFileName);
		customerRepository.save(customer);
	}
}
