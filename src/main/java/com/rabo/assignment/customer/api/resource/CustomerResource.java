package com.rabo.assignment.customer.api.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.rabo.assignment.customer.api.model.Customer;
import com.rabo.assignment.customer.api.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerResource {

	@Autowired
	private CustomerRepository customerRepository;

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
		customer.setCurrentAddress(customerRequest.getAddress());

		Customer newCustomer = customerRepository.save(customer);

		return ResponseEntity.ok(newCustomer.getId());
	}

	/**
	 * @return
	 */
	@GetMapping(name = "/all", produces = APPLICATION_JSON_VALUE)
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
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if (optionalCustomer.isPresent()) {
			return ResponseEntity.ok(optionalCustomer.get());
		} else {
			throw new RuntimeException("");
		}
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Customer>> getCustomerByName(
			@RequestParam(name = "firstName", required = false) Optional<String> firstName,
			@RequestParam(name = "lastName", required = false) Optional<String> lastName) {
		List<Customer> result = new ArrayList<>();
		if (firstName.isPresent()) {
			result.addAll(customerRepository.findByFirstName(firstName.get()));
		} else if (lastName.isPresent()) {
			result.addAll(customerRepository.findByLastName(lastName.get()));
		}
		return ResponseEntity.ok(result);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCustomerAddress(@PathVariable("id") String id,
			@RequestParam Map<String, String> updates) {
		return ResponseEntity.noContent().build();

	}

}
