package com.example.customer.api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer.api.exception.CustomerNotFoundException;
import com.example.customer.api.model.CustomerAddressRequest;
import com.example.customer.api.model.CustomerRequest;
import com.example.customer.api.util.UniqueIdGenerator;
import com.example.customer.data.model.Address;
import com.example.customer.data.model.Customer;
import com.example.customer.service.CustomerService;

/**
 * Customer rest controller class contains all customer related operations
 * implemented as part of this.
 * 
 * @author akshay
 *
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	/**
	 * POST request for customer. Adds new customer with all mandatory details.
	 * 
	 * @param customerRequest request body with all details of customer.
	 * @return newly generated customer id.
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Long> addNewCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
		LOG.info("Add new customer method is called");

		Customer customer = new Customer();
		customer.setCustomerId(UniqueIdGenerator.nextID());
		customer.setFirstName(customerRequest.getFirstName());
		customer.setLastName(customerRequest.getLastName());
		customer.setDateOfBirth(customerRequest.getDateOfBirth());

		Period period = Period.between(customerRequest.getDateOfBirth(), LocalDate.now());
		customer.setAge(period.getYears());

		CustomerAddressRequest address = customerRequest.getAddress();
		Address currentLivingAddress = new Address();
		currentLivingAddress.setCity(address.getCity());
		currentLivingAddress.setHouseNumber(address.getHouseNumber());
		currentLivingAddress.setStreet(address.getStreet());
		currentLivingAddress.setZipCode(address.getZipCode());
		currentLivingAddress.setCustomer(customer);

		customer.setCurrentLivingAddress(currentLivingAddress);

		Long customerId = customerService.addNewCustomer(customer);

		return ResponseEntity.ok(customerId);
	}

	/**
	 * Get customer for specific Id.
	 * 
	 * @param customerId for which customer details needs to be fetched.
	 * @return {@link Customer} if present, or {@link CustomerNotFoundException}
	 *         will be thrown with {@link HttpStatus#NOT_FOUND}
	 */
	@GetMapping(value = "/{customerId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId) {
		LOG.info("Get Customer by id");
		Customer customer = customerService.getCustomerById(customerId);
		return ResponseEntity.ok(customer);
	}

	/**
	 * Get all customers
	 * 
	 * @param pageNumber for pagination page number can be used with default size of
	 *                   100 elements per page
	 * @return list of {@link Customer} with matching criteria
	 */
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Customer>> getAllCustomers(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber) {
		LOG.info("Get All Customers");
		List<Customer> result = new ArrayList<>();
		result.addAll(customerService.getAllCustomers(pageNumber));
		return ResponseEntity.ok(result);
	}

	/**
	 * Search for customer based on first name and/or last name of customer, all
	 * parameters are optional for search.
	 * 
	 * @param firstName first name of customer to be searched.
	 * @param lastName  last name of customer to be searched.
	 * @return list of {@link Customer} with matching criteria or else empty list
	 *         will be returned.
	 */
	@GetMapping(value = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Customer>> getCustomers(
			@RequestParam(name = "firstName", required = false) Optional<String> firstName,
			@RequestParam(name = "lastName", required = false) Optional<String> lastName) {
		LOG.info("Get All Customers or filter based on criteria");
		List<Customer> result = new ArrayList<>();
		if (firstName.isPresent() || lastName.isPresent()) {
			result.addAll(customerService.getCustomersByName(firstName.orElse(""), lastName.orElse("")));
		}
		return ResponseEntity.ok(result);
	}

	/**
	 * Update customer address for specific customer.
	 * 
	 * @param customerId customer id for which address needs to be updated.
	 * @param address    Customer address body with all details of address.
	 * @return no content will be returned, in case of customer not found in
	 *         directory {@link CustomerNotFoundException} will be thrown with
	 *         {@link HttpStatus#NOT_FOUND}
	 */
	@PutMapping(value = "/{customerId}/address", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateCustomerAddress(@PathVariable("customerId") Long customerId,
			@Valid @RequestBody CustomerAddressRequest address) {
		LOG.info("Update customer address for customer id");
		Address currentLivingAddress = new Address();

		currentLivingAddress.setCity(address.getCity());
		currentLivingAddress.setHouseNumber(address.getHouseNumber());
		currentLivingAddress.setStreet(address.getStreet());
		currentLivingAddress.setZipCode(address.getZipCode());

		customerService.updateCustomerAddress(customerId, currentLivingAddress);

		return ResponseEntity.noContent().build();

	}

}
