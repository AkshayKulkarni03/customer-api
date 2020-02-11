package com.rabo.assignment.customer.api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Address of customer. Address have one to one mapping with customer while
 * keeping same Id as customer id as unique key for entity
 * 
 * @author akshay
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Address implements Serializable {

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private static final long serialVersionUID = 1128347883953906369L;

	@Id
	@Setter(value = AccessLevel.PROTECTED)
	@Getter(value = AccessLevel.PROTECTED)
	private String id;
	private String street;
	private String houseNumber;
	private String city;
	private String zipCode;

	@OneToOne
	@MapsId
	@Getter(value = AccessLevel.PROTECTED)
	private Customer customer;
}
