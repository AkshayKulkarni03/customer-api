package com.rabo.assignment.customer.api.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rabo.assignment.customer.api.exception.CustomerNotFoundException;
import com.rabo.assignment.customer.api.model.Address;
import com.rabo.assignment.customer.api.model.Customer;
import com.rabo.assignment.customer.api.repository.AddressRepository;
import com.rabo.assignment.customer.api.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerResource {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;

	/**
	 * @param customerRequest
	 * @return
	 */
	@PostMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addNewCsutomer(@RequestBody CustomerRequest customerRequest) {

		Customer customer = new Customer();
		customer.setFirstName(customerRequest.getFirstName());
		customer.setLastName(customerRequest.getLastName());
		customer.setAge(customerRequest.getAge());

		CustomerAddressRequest address = customerRequest.getAddress();
		Address currentLivingAddress = new Address();
		currentLivingAddress.setCity(address.getCity());
		currentLivingAddress.setHouseNumber(address.getHouseNumber());
		currentLivingAddress.setStreet(address.getStreet());
		currentLivingAddress.setZipCode(address.getZipCode());
		currentLivingAddress.setCustomer(customer);

		customer.setCurrentLivingAddress(currentLivingAddress);

		Customer newCustomer = customerRepository.saveAndFlush(customer);

		return ResponseEntity.ok(newCustomer.getId());
	}

	/**
	 * @return
	 */
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> result = new ArrayList<>();
		Iterable<Customer> all = customerRepository.findAll();
		all.iterator().forEachRemaining(result::add);
		return ResponseEntity.ok(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		Customer customer = optionalCustomer
				.orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));
		return ResponseEntity.ok(customer);
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	@GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Customer>> getCustomerByName(
			@RequestParam(name = "firstName", required = false) Optional<String> firstName,
			@RequestParam(name = "lastName", required = false) Optional<String> lastName) {
		List<Customer> result = new ArrayList<>();
		if (firstName.isPresent() && lastName.isPresent()) {
			result.addAll(customerRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase(firstName.get(),
					lastName.get()));
		} else if (firstName.isPresent()) {
			result.addAll(customerRepository.findByFirstNameIgnoreCase(firstName.get()));
		} else if (lastName.isPresent()) {
			result.addAll(customerRepository.findByLastNameIgnoreCase(lastName.get()));
		}
		return ResponseEntity.ok(result);
	}

	/**
	 * @param id
	 * @param address
	 * @return
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCustomerAddress(@PathVariable("id") String id,
			@RequestBody CustomerAddressRequest address) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		Customer customer = optionalCustomer
				.orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));

		Optional<Address> optionalAddress = addressRepository.findById(customer.getId());
		Address currentLivingAddress = optionalAddress.orElseThrow(
				() -> new CustomerNotFoundException(String.format("Customer Address for Id %s not found", id)));

		currentLivingAddress.setCity(address.getCity());
		currentLivingAddress.setHouseNumber(address.getHouseNumber());
		currentLivingAddress.setStreet(address.getStreet());
		currentLivingAddress.setZipCode(address.getZipCode());

		addressRepository.saveAndFlush(currentLivingAddress);

		return ResponseEntity.noContent().build();

	}

}
