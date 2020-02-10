package com.rabo.assignment.customer.api.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
