package com.rabo.assignment.customer.api.resource;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NotNull
@Getter
@Setter
@NoArgsConstructor
public class CustomerRequest {

	private String firstName;
	private String lastName;
	private Integer age;
	private String address;

}
