package com.rabo.assignment.customer.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rabo.assignment.customer.data.model.Address;

/**
 * Address repository with all default CRUD operation implementation for
 * {@link Address} table with JPA
 * 
 * @see JpaRepository
 * @author akshay
 *
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
