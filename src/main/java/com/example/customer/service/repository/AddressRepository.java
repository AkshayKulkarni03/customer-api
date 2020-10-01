package com.example.customer.service.repository;

import com.example.customer.data.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Address repository with all default CRUD operation implementation for
 * {@link Address} table with JPA
 *
 * @author akshay
 * @see JpaRepository
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
