package com.rabo.assignment.customer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rabo.assignment.customer.model.Customer;

/**
 * @author akshay
 *
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

	/**
	 * @param firstName
	 * @return
	 */
	public List<Customer> findByFirstName(String firstName);

	/**
	 * @param lastName
	 * @return
	 */
	public List<Customer> findByLastName(String lastName);
}
