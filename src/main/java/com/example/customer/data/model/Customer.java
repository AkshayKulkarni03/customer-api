package com.example.customer.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customer model class with JPA implementations with all information related to
 * customer. Contains one to one mapping with address of the customer.
 * 
 * @see Entity
 * @see OneToOne
 * @author akshay
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Customer implements Serializable {

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private static final long serialVersionUID = -3946148493430610912L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(unique = true, nullable = false)
    private Long customerId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Integer age;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private Address currentLivingAddress;

}
