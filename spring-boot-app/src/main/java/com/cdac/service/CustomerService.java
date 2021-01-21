package com.cdac.service;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dto.Login;
import com.cdac.entity.Customer;
import com.cdac.exception.CustomerServiceException;
import com.cdac.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public int register(Customer customer) {
		if(!customerRepository.isCustomerPresent(customer.getEmail())) {
			return customerRepository.save(customer);
			//suppose we want to send an email confirmation
			//then that code will be here..
		}
		else
			throw new CustomerServiceException("Customer already registered!");
	}
	
	public Customer login(Login login) {
		try {
			if(customerRepository.isCustomerPresent(login.getEmail())) {
				int id = customerRepository.fetch(login.getEmail(), login.getPassword());
				Customer customer = customerRepository.fetch(id);
				return customer;
			}
			else
				throw new CustomerServiceException("Customer does not exist!");
		}
		catch(EmptyResultDataAccessException e) {
			throw new CustomerServiceException("Incorrect Email/Password");
		}
	}
	
	public Customer get(int customerId) {
		return customerRepository.fetch(customerId);
	}
	
	
}
