package com.rabo.assignment.customer.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private Integer age;
	private String currentAddress;

}
