package com.cdac.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.cdac.entity.Customer;

@Repository
public class CustomerRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public int save(Customer customer) {
		Customer updatedCustomer = entityManager.merge(customer);
		return updatedCustomer.getId();
	}
	
	public Customer fetch(int id) {
		return entityManager.find(Customer.class, id);
	}
	
	public boolean isCustomerPresent(String email) {
		return (Long)
				entityManager
				.createQuery("select count(c.id) from Customer c where c.email = :email") //JPQL/HQL
				.setParameter("email", email)
				.getSingleResult() == 1 ? true : false;
	}
	
	//return id of the customer
	public int fetch(String email, String password) {
		/*Query q = entityManager.createQuery("select c.id from Customer c where c.email = :email and c.password = :password");
		q.setParameter("email", email);
		q.setParameter("password", password);
		Integer id = (Integer) q.getSingleResult();
		return id;*/
		
		return (Integer)
				entityManager
				.createQuery("select c.id from Customer c where c.email = :email and c.password = :password")
				.setParameter("email", email)
				.setParameter("password", password)
				.getSingleResult();
	}
}
