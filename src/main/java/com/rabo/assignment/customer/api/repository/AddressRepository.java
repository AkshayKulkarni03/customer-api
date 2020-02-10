package com.rabo.assignment.customer.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rabo.assignment.customer.api.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
