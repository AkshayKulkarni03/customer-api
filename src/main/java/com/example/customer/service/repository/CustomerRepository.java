package com.example.customer.service.repository;

import com.example.customer.data.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Customer repository with all default CRUD operation implementation for
 * {@link Customer} table with JPA
 *
 * @author akshay
 * @see JpaRepository
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c from Customer c where c.customerId = ?1")
    Optional<Customer> findByCustomerId(Long customerId);

    /**
     * Method to Search customer by its first name. Search performed is case
     * insensitive, but its an exact match
     *
     * @param firstName name of the customer to be matched.
     * @param pageable  paging restriction provided in the {@link Pageable} object
     * @return {@link List} of {@link Customer} with matched criteria or empty list
     * returned
     */
    public List<Customer> findByFirstNameIgnoreCase(String firstName, Pageable pageable);

    /**
     * Method to Search customer by its last name. Search performed is case
     * insensitive, but its an exact match
     *
     * @param lastName last name of the customer to be matched.
     * @param pageable paging restriction provided in the {@link Pageable} object
     * @return {@link List} of {@link Customer} with matched criteria or empty list
     * returned.
     */
    public List<Customer> findByLastNameIgnoreCase(String lastName, Pageable pageable);

    /**
     * Method to Search customer by its last name. Search performed is case
     * insensitive, but its an exact match
     *
     * @param firstName name of the customer to be matched.
     * @param lastName  last name of the customer to be matched.
     * @param pageable  paging restriction provided in the {@link Pageable} object
     * @return {@link List} of {@link Customer} with matched criteria or empty list
     * returned.
     */
    public List<Customer> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase(String firstName, String lastName,
                                                                                   Pageable pageable);
}
