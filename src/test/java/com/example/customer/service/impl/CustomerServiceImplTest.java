package com.example.customer.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.customer.api.exception.CustomerNotFoundException;
import com.example.customer.data.model.Address;
import com.example.customer.data.model.Customer;
import com.example.customer.service.repository.AddressRepository;
import com.example.customer.service.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

	@InjectMocks
	private CustomerServiceImpl unitToTest;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressRepository addressRepository;

	@Test
	void testAddNewCustomer() {
		Customer customer = new Customer();
		customer.setId("101");
		customer.setCustomerId(101L);
		when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

		Long result = unitToTest.addNewCustomer(getCustomer());

		assertEquals(101L, result);
	}

	@Test
	void testGetAllCustomers() {
		Customer customer1 = getCustomer();

		Customer customer2 = new Customer();
		customer2.setAge(20);
		customer2.setFirstName("FirstName2");
		customer2.setLastName("LastName2");

		Address address2 = new Address();
		address2.setCity("TestCity2");
		address2.setHouseNumber("420");
		address2.setStreet("TestStreet2");
		address2.setZipCode("TestZipCode");
		address2.setCustomer(customer2);

		customer2.setCurrentLivingAddress(address2);

		List<Customer> list = new ArrayList<>();
		list.addAll(Arrays.asList(customer1, customer2));
		Page<Customer> pagedTasks = new PageImpl<Customer>(list);
		when(customerRepository.findAll(any(Pageable.class))).thenReturn(pagedTasks);

		List<Customer> result = unitToTest.getAllCustomers(0);

		assertThat(result).isEqualTo(list);
	}

	@Test
	void testGetCustomerByFirstName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);
		when(customerRepository.findByFirstNameIgnoreCase("firstName1")).thenReturn(list);

		List<Customer> result = unitToTest.getCustomersByName("firstName1", "");
		assertThat(result).isEqualTo(list);
	}

	@Test
	void testGetCustomerByLastName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);

		when(customerRepository.findByLastNameIgnoreCase("lastname1")).thenReturn(list);

		List<Customer> result = unitToTest.getCustomersByName(null, "lastname1");
		assertThat(result).isEqualTo(list);
	}

	@Test
	void testGetCustomerByFirstNameAndLastName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);
		when(customerRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase("firstName1", "lastname1"))
				.thenReturn(list);

		List<Customer> result = unitToTest.getCustomersByName("firstName1", "lastname1");
		assertThat(result).isEqualTo(list);
	}

	@Test
	void testGetCustomerById() {
		Customer customer = getCustomer();

		when(customerRepository.findByCustomerId(101L)).thenReturn(Optional.of(customer));

		Customer result = unitToTest.getCustomerById(101L);
		assertThat(result).isEqualTo(customer);
	}

	@Test
	void testGetCustomerByIdThrowsException() {
		when(customerRepository.findByCustomerId(102L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.getCustomerById(102L);
		});
		assertEquals("Customer with Id 102 not found", exception.getMessage());
	}

	@Test
	void testUpdateCustomerAddress() {
		Customer customer = getCustomer();
		Address address = customer.getCurrentLivingAddress();

		Address inputAddressTobeAdded = new Address();
		inputAddressTobeAdded.setCity("Test City");
		inputAddressTobeAdded.setHouseNumber("House Number");
		inputAddressTobeAdded.setStreet("Test Street");
		inputAddressTobeAdded.setZipCode("Test ZipCode");

		when(customerRepository.findByCustomerId(101L)).thenReturn(Optional.of(customer));
		when(addressRepository.findById("101")).thenReturn(Optional.of(address));

		unitToTest.updateCustomerAddress(101L, inputAddressTobeAdded);

		assertThat(address.getZipCode()).isEqualTo(inputAddressTobeAdded.getZipCode());
	}

	@Test
	void testUpdateCustomerAddressCustomerNotFound() {
		Address inputAddressTobeAdded = new Address();
		inputAddressTobeAdded.setCity("Test City");
		inputAddressTobeAdded.setHouseNumber("House Number");
		inputAddressTobeAdded.setStreet("Test Street");
		inputAddressTobeAdded.setZipCode("Test ZipCode");

		when(customerRepository.findByCustomerId(102L)).thenReturn(Optional.empty());
		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.updateCustomerAddress(102L, inputAddressTobeAdded);
		});
		assertEquals("Customer with Id 102 not found", exception.getMessage());
	}

	@Test
	void testUpdateCustomerAddressCustomerAddressNotFound() {
		Customer customer = getCustomer();
		Address inputAddressTobeAdded = new Address();
		inputAddressTobeAdded.setCity("Test City");
		inputAddressTobeAdded.setHouseNumber("House Number");
		inputAddressTobeAdded.setStreet("Test Street");
		inputAddressTobeAdded.setZipCode("Test ZipCode");

		when(customerRepository.findByCustomerId(102L)).thenReturn(Optional.of(customer));
		when(addressRepository.findById("101")).thenReturn(Optional.empty());
		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.updateCustomerAddress(102L, inputAddressTobeAdded);
		});
		assertEquals("Customer Address for Id 102 not found", exception.getMessage());
	}

	private Customer getCustomer() {
		Customer customer = new Customer();
		customer.setId("101");
		customer.setCustomerId(101L);
		customer.setAge(20);
		customer.setFirstName("FirstName1");
		customer.setLastName("LastName1");
		customer.setDateOfBirth(LocalDate.of(1998, 12, 1));
		customer.setAge(22);

		Address address = new Address();
		address.setCity("TestCity1");
		address.setHouseNumber("400");
		address.setStreet("TestStreet1");
		address.setZipCode("TestZipCode");
		address.setCustomer(customer);
		customer.setCurrentLivingAddress(address);
		return customer;
	}

}
