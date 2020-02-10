package com.rabo.assignment.customer.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rabo.assignment.customer.api.model.Customer;

/**
 * @author akshay
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Method to Search customer by its first name. Search performed is case
     * insensitive, but its an exact match
     * 
     * @param firstName
     *            name of the customer to be matched.
     * @return {@link List} of {@link Customer} with matched criteria or empty
     *         list returned
     */
    public List<Customer> findByFirstNameIgnoreCase(String firstName);

    /**
     * Method to Search customer by its last name. Search performed is case
     * insensitive, but its an exact match
     * 
     * @param lastName
     *            last name of the customer to be matched.
     * @return {@link List} of {@link Customer} with matched criteria or empty
     *         list returned.
     */
    public List<Customer> findByLastNameIgnoreCase(String lastName);
}
