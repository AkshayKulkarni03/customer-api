package com.rabo.assignment.customer.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rabo.assignment.customer.api.model.Customer;

/**
 * @author akshay
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, String> {

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
