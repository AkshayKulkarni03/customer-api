package com.rabo.assignment.customer.api.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotBlank(message = "First Name of customer can not be empty")
    private String firstName;
    @NotEmpty(message = "Last Name of customer can not be empty")
    private String lastName;
    private Integer age;
    @Valid
    private CustomerAddressRequest address;

}
