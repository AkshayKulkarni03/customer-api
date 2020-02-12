package com.rabo.assignment.customer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rabo.assignment.customer.api.exception.CustomerNotFoundException;
import com.rabo.assignment.customer.data.model.Address;
import com.rabo.assignment.customer.data.model.Customer;
import com.rabo.assignment.customer.service.CustomerService;
import com.rabo.assignment.customer.service.repository.AddressRepository;
import com.rabo.assignment.customer.service.repository.CustomerRepository;

/**
 * Customer Service implementation for all customer related operations
 * 
 * @see CustomerService
 * @author akshay
 *
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Long addNewCustomer(Customer customer) {
        Customer newCustomer = customerRepository.saveAndFlush(customer);
        return newCustomer.getCustomerId();
    }

    @Override
    public List<Customer> getAllCustomers(Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 100, Sort.by("firstName"));
        List<Customer> result = new ArrayList<>();
        Iterable<Customer> all = customerRepository.findAll(pageRequest);
        all.iterator().forEachRemaining(result::add);
        return result;
    }

    @Override
    public List<Customer> getCustomersByName(String firstName, String lastName) {
        List<Customer> result = new ArrayList<>();
        if (StringUtils.isNoneBlank(firstName, lastName)) {
            result.addAll(customerRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase(firstName, lastName));
        } else if (StringUtils.isNotBlank(firstName)) {
            result.addAll(customerRepository.findByFirstNameIgnoreCase(firstName));
        } else if (StringUtils.isNotBlank(lastName)) {
            result.addAll(customerRepository.findByLastNameIgnoreCase(lastName));
        }
        return result;
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(id);
        return optionalCustomer.orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));
    }

    @Override
    public void updateCustomerAddress(Long id, Address address) {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(id);
        Customer customer = optionalCustomer.orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));

        Optional<Address> optionalAddress = addressRepository.findById(customer.getId());
        Address currentLivingAddress = optionalAddress
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer Address for Id %s not found", id)));

        currentLivingAddress.setCity(address.getCity());
        currentLivingAddress.setHouseNumber(address.getHouseNumber());
        currentLivingAddress.setStreet(address.getStreet());
        currentLivingAddress.setZipCode(address.getZipCode());

        addressRepository.saveAndFlush(currentLivingAddress);
    }

}
