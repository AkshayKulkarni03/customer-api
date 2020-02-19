package com.example.customer.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.customer.data.model.Customer;

/**
 * Customer repository with all default CRUD operation implementation for
 * {@link Customer} table with JPA
 * 
 * @see JpaRepository
 * @author akshay
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    public Optional<Customer> findByCustomerId(Long customerId);

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

    /**
     * Method to Search customer by its last name. Search performed is case
     * insensitive, but its an exact match
     * 
     * @param firstName
     *            name of the customer to be matched.
     * @param lastName
     *            last name of the customer to be matched.
     * @return {@link List} of {@link Customer} with matched criteria or empty
     *         list returned.
     */
    public List<Customer> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase(String firstName, String lastName);
}
