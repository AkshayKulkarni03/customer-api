package com.example.customer.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    @Sql({"schema.sql", "data.sql"})
    public static void setUpBeforeAll() throws Exception {
    }

    @Test
    void testAddNewCustomer() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"TestLastName\", \"dateOfBirth\":\"1997-03-12\", \"address\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"400\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andDo(print()).andExpect(status().isOk());
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"TestLastName\", \"dateOfBirth\":\"1997-03-12\", \"address\": { \"street\": \"SOMESTREET\",\"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].message", is("House number is mandatory for address")));
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"\", \"dateOfBirth\":\"1997-03-12\", \"address\": { \"houseNumber\": \"400\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[{\"id\":\"1002\",\"customerId\":1002,\"firstName\":\"Charles\",\"lastName\":\"Bailey\",\"dateOfBirth\":\"1991-12-01\",\"age\":28,\"currentLivingAddress\":{\"street\":\"SOMESTREET\",\"houseNumber\":\"420\",\"city\":\"Utrecht\",\"zipCode\":\"2928ZP\"}},{\"id\":\"1003\",\"customerId\":1003,\"firstName\":\"Chloe\",\"lastName\":\"Forsyth\",\"dateOfBirth\":\"1995-01-11\",\"age\":25,\"currentLivingAddress\":{\"street\":\"SOMESTREET\",\"houseNumber\":\"103a\",\"city\":\"De Hauge\",\"zipCode\":\"3928ZP\"}},{\"id\":\"1001\",\"customerId\":1001,\"firstName\":\"John\",\"lastName\":\"Doe\",\"dateOfBirth\":\"2000-01-20\",\"age\":20,\"currentLivingAddress\":{\"street\":\"SOMESTREET\",\"houseNumber\":\"200\",\"city\":\"Amsterdam\",\"zipCode\":\"1928ZP\"}},{\"id\":\"1005\",\"customerId\":1005,\"firstName\":\"Rik\",\"lastName\":\"Rik\",\"dateOfBirth\":\"1987-03-14\",\"age\":32,\"currentLivingAddress\":{\"street\":\"Postweg\",\"houseNumber\":\"81\",\"city\":\"Buren\",\"zipCode\":\"9164LS\"}},{\"id\":\"1004\",\"customerId\":1004,\"firstName\":\"Selman\",\"lastName\":\"Schriemer\",\"dateOfBirth\":\"1957-11-23\",\"age\":62,\"currentLivingAddress\":{\"street\":\"Kastanjestraat\",\"houseNumber\":\"137\",\"city\":\"Schagen\",\"zipCode\":\"1741WK\"}}]"));

    }

    @Test
    void testGetAllCustomersWithRestTemplate() throws Exception {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/customers", List.class);
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/customers/1001")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "{\"id\":\"1001\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"age\":20,\"currentLivingAddress\":{\"street\":\"SOMESTREET\",\"houseNumber\":\"200\",\"city\":\"Amsterdam\",\"zipCode\":\"1928ZP\"}}"));

        mockMvc.perform(get("/customers/2001")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Customer with Id 2001 not found")))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())));

    }

    @Test
    void testGetCustomerByName() throws Exception {
        mockMvc.perform(get("/customers?firstName=john")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(
                        "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
        mockMvc.perform(get("/customers?lastName=doE")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(
                        "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
        mockMvc.perform(get("/customers?firstName=john&lastName=doE")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(
                        "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
    }

    @Test
    void testUpdateCustomerAddress() throws Exception {
        mockMvc.perform(put("/customers/1003/address").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"street\": \"SOMESTREESAT\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }"))
                .andDo(print()).andExpect(status().isNoContent());

        mockMvc.perform(put("/customers/2001/address").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"street\": \"SOMESTREESAT\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }"))
                .andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Customer with Id 2001 not found")))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())));

        mockMvc.perform(put("/customers/1003/address").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"street\": \"\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].message", is("Street name is mandatory for address")));
    }

}



