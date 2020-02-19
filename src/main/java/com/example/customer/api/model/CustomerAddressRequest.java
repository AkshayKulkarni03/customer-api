package com.example.customer.api.model;

import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "Street name is mandatory for address")
    private String street;
    @NotBlank(message = "House number is mandatory for address")
    private String houseNumber;
    @NotBlank(message = "City name is mandatory for address")
    private String city;
    @NotBlank(message = "Zip Code is mandatory for address")
    private String zipCode;

}
