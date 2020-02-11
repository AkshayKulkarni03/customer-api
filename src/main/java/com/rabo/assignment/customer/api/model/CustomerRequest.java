package com.rabo.assignment.customer.api.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customer request body for adding new customer.
 * 
 * @author akshay
 *
 */
@NotNull
@Getter
@Setter
@NoArgsConstructor
public class CustomerRequest {

	private String firstName;
	private String lastName;
	private Integer age;
	private CustomerAddressRequest address;

}
