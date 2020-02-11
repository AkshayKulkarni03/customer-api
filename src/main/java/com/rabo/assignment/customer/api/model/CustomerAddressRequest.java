package com.rabo.assignment.customer.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customer address request for new customer and updating customers address.
 * 
 * @author akshay
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressRequest {

	private String street;
	private String houseNumber;
	private String city;
	private String zipCode;

}
