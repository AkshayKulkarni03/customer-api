package com.rabo.assignment.customer.resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabo.assignment.customer.model.Customer;
import com.rabo.assignment.customer.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerResource {

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * @param customer
	 * @return
	 */
	@PostMapping(produces = "application/json")
	public ResponseEntity<String> addNewCsutomer(@RequestBody Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(newCustomer.getId());
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> all = customerRepository.findAll();
		return ResponseEntity.ok(all);
	}

	@GetMapping
	public ResponseEntity<Customer> getCustomerById(String id) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if (optionalCustomer.isPresent()) {
			return ResponseEntity.ok(optionalCustomer.get());
		} else {
			throw new RuntimeException("");
		}
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getCustomerByName(Map<String, String> entity) {
		return ResponseEntity.ok(null);
	}

	@PatchMapping
	public ResponseEntity<Void> updateCustomerAddress(String id, String address) {
		return ResponseEntity.noContent().build();

	}

}
