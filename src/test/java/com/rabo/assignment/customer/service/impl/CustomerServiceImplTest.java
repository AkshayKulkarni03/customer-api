package com.rabo.assignment.customer.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rabo.assignment.customer.api.exception.CustomerNotFoundException;
import com.rabo.assignment.customer.data.model.Address;
import com.rabo.assignment.customer.data.model.Customer;
import com.rabo.assignment.customer.service.repository.AddressRepository;
import com.rabo.assignment.customer.service.repository.CustomerRepository;

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
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        String result = unitToTest.addNewCustomer(getCustomer());

        assertEquals("101", result);
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

        when(customerRepository.findAll()).thenReturn(list);

        List<Customer> result = unitToTest.getAllCustomers();

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
        when(customerRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCase("firstName1", "lastname1")).thenReturn(list);

        List<Customer> result = unitToTest.getCustomersByName("firstName1", "lastname1");
        assertThat(result).isEqualTo(list);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = getCustomer();

        when(customerRepository.findById("101")).thenReturn(Optional.of(customer));

        Customer result = unitToTest.getCustomerById("101");
        assertThat(result).isEqualTo(customer);
    }

    @Test
    void testGetCustomerByIdThrowsException() {
        when(customerRepository.findById("102")).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            unitToTest.getCustomerById("102");
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

        when(customerRepository.findById("101")).thenReturn(Optional.of(customer));
        when(addressRepository.findById("101")).thenReturn(Optional.of(address));

        unitToTest.updateCustomerAddress("101", inputAddressTobeAdded);

        assertThat(address.getZipCode()).isEqualTo(inputAddressTobeAdded.getZipCode());
    }

    @Test
    void testUpdateCustomerAddressCustomerNotFound() {
        Address inputAddressTobeAdded = new Address();
        inputAddressTobeAdded.setCity("Test City");
        inputAddressTobeAdded.setHouseNumber("House Number");
        inputAddressTobeAdded.setStreet("Test Street");
        inputAddressTobeAdded.setZipCode("Test ZipCode");

        when(customerRepository.findById("102")).thenReturn(Optional.empty());
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            unitToTest.updateCustomerAddress("102", inputAddressTobeAdded);
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

        when(customerRepository.findById("102")).thenReturn(Optional.of(customer));
        when(addressRepository.findById("101")).thenReturn(Optional.empty());
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            unitToTest.updateCustomerAddress("102", inputAddressTobeAdded);
        });
        assertEquals("Customer Address for Id 102 not found", exception.getMessage());
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setId("101");
        customer.setAge(20);
        customer.setFirstName("FirstName1");
        customer.setLastName("LastName1");

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
