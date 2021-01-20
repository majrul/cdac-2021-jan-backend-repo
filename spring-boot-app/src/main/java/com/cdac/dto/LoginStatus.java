package com.cdac.dto;

public class LoginStatus extends Status {

	//if you want to return all the details
	//then we might do this:
	//private Customer customer;
	private int customerId;
	private String name;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
