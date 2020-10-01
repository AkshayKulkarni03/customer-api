package com.example.customer.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.customer.api.exception.CustomerNotFoundException;
import com.example.customer.api.model.CustomerAddressRequest;
import com.example.customer.api.model.CustomerRequest;
import com.example.customer.data.model.Address;
import com.example.customer.data.model.Customer;
import com.example.customer.service.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

	@InjectMocks
	private CustomerController unitToTest;

	@Mock
	private CustomerService customerService;

	@Test
	void testAddNewCustomer() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(customerService.addNewCustomer(any(Customer.class))).thenReturn(101L);

		CustomerRequest customerToAdd = new CustomerRequest();
		customerToAdd.setFirstName("Test FirstName");
		customerToAdd.setLastName("Test LastName");
		customerToAdd.setDateOfBirth(LocalDate.of(2000, 1, 12));

		CustomerAddressRequest currentLivingAddress = new CustomerAddressRequest("Test Street", "House Number",
				"Test City", "Test ZipCode");

		customerToAdd.setAddress(currentLivingAddress);

		ResponseEntity<Long> responseEntity = unitToTest.addNewCustomer(customerToAdd);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(101L);

	}

	@Test
	void testGetAllCustomers() {
		Customer customer1 = getCustomer();

		Customer customer2 = new Customer();
		customer2.setFirstName("FirstName2");
		customer2.setLastName("LastName2");
		customer2.setDateOfBirth(LocalDate.of(1999, 12, 1));
		customer2.setAge(21);

		Address address2 = new Address();
		address2.setCity("TestCity2");
		address2.setHouseNumber("420");
		address2.setStreet("TestStreet2");
		address2.setZipCode("TestZipCode");
		address2.setCustomer(customer2);

		customer2.setCurrentLivingAddress(address2);

		ArrayList<Customer> list = new ArrayList<>();
		list.addAll(Arrays.asList(customer1, customer2));

		when(customerService.getAllCustomers(0)).thenReturn(list);

		ResponseEntity<List<Customer>> responseEntity = unitToTest.getCustomers(Optional.empty(), Optional.empty(), 0);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().size()).isEqualTo(2);
		assertThat(responseEntity.getBody().get(0).getFirstName()).isEqualTo(customer1.getFirstName());
		assertThat(responseEntity.getBody().get(1).getFirstName()).isEqualTo(customer2.getFirstName());

	}

	@Test
	void testGetCustomerById() {
		Customer customer = getCustomer();

		when(customerService.getCustomerById(101L)).thenReturn(customer);

		ResponseEntity<Customer> responseEntity = unitToTest.getCustomerById(101L);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().getFirstName()).isEqualTo(customer.getFirstName());
	}

	@Test
	void testGetCustomerByIdThrowsException() {
		when(customerService.getCustomerById(102L))
				.thenThrow(new CustomerNotFoundException("Customer with Id 102 not found"));
		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.getCustomerById(102L);
		});
		assertEquals("Customer with Id 102 not found", exception.getMessage());
	}

	@Test
	void testGetCustomerByFirstName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);
		when(customerService.getCustomersByName(0, "firstName1", "")).thenReturn(list);

		ResponseEntity<List<Customer>> responseEntity = unitToTest.getCustomers(Optional.of("firstName1"),
				Optional.empty(), 0);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().size()).isEqualTo(1);
		assertThat(responseEntity.getBody().get(0).getFirstName()).isEqualTo(customer.getFirstName());
	}

	@Test
	void testGetCustomerByLastName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);

		when(customerService.getCustomersByName(0, "", "lastname1")).thenReturn(list);

		ResponseEntity<List<Customer>> responseEntity = unitToTest.getCustomers(Optional.empty(),
				Optional.of("lastname1"), 0);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().size()).isEqualTo(1);
		assertThat(responseEntity.getBody().get(0).getLastName()).isEqualTo(customer.getLastName());
	}

	@Test
	void testGetCustomerByFirstNameAndLastName() {
		Customer customer = getCustomer();
		List<Customer> list = new ArrayList<>();
		list.add(customer);
		when(customerService.getCustomersByName(0, "firstName1", "lastname1")).thenReturn(list);

		ResponseEntity<List<Customer>> responseEntity = unitToTest.getCustomers(Optional.of("firstName1"),
				Optional.of("lastname1"), 0);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().size()).isEqualTo(1);
		assertThat(responseEntity.getBody().get(0).getFirstName()).isEqualTo(customer.getFirstName());
	}

	@Test
	void testUpdateCustomerAddress() {
		CustomerAddressRequest currentLivingAddress = new CustomerAddressRequest();
		currentLivingAddress.setCity("Test City");
		currentLivingAddress.setHouseNumber("House Number");
		currentLivingAddress.setStreet("Test Street");
		currentLivingAddress.setZipCode("Test ZipCode");

		doNothing().when(customerService).updateCustomerAddress(eq(101L), any(Address.class));

		ResponseEntity<Void> responseEntity = unitToTest.updateCustomerAddress(101L, currentLivingAddress);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

	}

	@Test
	void testUpdateCustomerAddressCustomerNotFound() {
		CustomerAddressRequest currentLivingAddress = new CustomerAddressRequest();
		currentLivingAddress.setCity("Test City");
		currentLivingAddress.setHouseNumber("House Number");
		currentLivingAddress.setStreet("Test Street");
		currentLivingAddress.setZipCode("Test ZipCode");

		doThrow(new CustomerNotFoundException("Customer with Id 102 not found")).when(customerService)
				.updateCustomerAddress(eq(102L), any(Address.class));
		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.updateCustomerAddress(102L, currentLivingAddress);
		});
		assertEquals("Customer with Id 102 not found", exception.getMessage());
	}

	@Test
	void testUpdateCustomerAddressCustomerAddressNotFound() {
		CustomerAddressRequest currentLivingAddress = new CustomerAddressRequest();
		currentLivingAddress.setCity("Test City");
		currentLivingAddress.setHouseNumber("House Number");
		currentLivingAddress.setStreet("Test Street");
		currentLivingAddress.setZipCode("Test ZipCode");

		doThrow(new CustomerNotFoundException("Customer Address for Id 102 not found")).when(customerService)
				.updateCustomerAddress(eq(102L), any(Address.class));

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			unitToTest.updateCustomerAddress(102L, currentLivingAddress);
		});
		assertEquals("Customer Address for Id 102 not found", exception.getMessage());
	}

	private Customer getCustomer() {
		Customer customer = new Customer();
		customer.setId("9e120341-627f-32be-8393-58b5d655b751");
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
