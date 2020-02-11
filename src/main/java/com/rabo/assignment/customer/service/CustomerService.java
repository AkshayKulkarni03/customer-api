package com.rabo.assignment.customer.service;

import java.util.List;

import com.rabo.assignment.customer.data.model.Address;
import com.rabo.assignment.customer.data.model.Customer;

/**
 * Customer service, specifies all operations for a customer
 * 
 * @author akshay
 *
 */
public interface CustomerService {

    /**
     * Add new customer to customer repository
     * 
     * @param customer
     *            {@link Customer} information of the customer
     * @return unique id assigned to the customer.
     */
    public String addNewCustomer(Customer customer);

    /**
     * Get list of all customers without any filters
     * 
     * @return list of {@link Customer}s
     */
    public List<Customer> getAllCustomers();

    /**
     * Find customer by first name and/or last name
     * 
     * @param firstName
     *            first name of customer to be searched
     * @param lastName
     *            last name of customer to be searched.
     * @return list of {@link Customer}s matching criteria.
     */
    public List<Customer> getCustomersByName(String firstName, String lastName);

    /**
     * Find customer by its unique id
     * 
     * @param id
     *            unique id of the customer to be searched
     * @return {@link Customer} matching criteria
     */
    public Customer getCustomerById(String id);

    /**
     * Update the customers address.
     * 
     * @param id
     *            unique id of the customer for which address needs to be
     *            updated
     * @param address
     *            {@link Address} of the customer to be added
     */
    public void updateCustomerAddress(String id, Address address);
}
