package com.rabo.assignment.customer.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    @Sql({ "schema.sql", "data.sql" })
    public static void setUpBeforeAll() throws Exception {
    }

    @Test
    void testAddNewCustomer() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"TestLastName\", \"age\": 29, \"address\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"400\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andDo(print()).andExpect(status().isOk());
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"TestLastName\", \"age\": 29, \"address\": { \"street\": \"SOMESTREET\",\"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.[0].message", is("House number is mandatory for address")));
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"firstName\": \"TestFirstName\", \"lastName\": \"\", \"age\": 29, \"address\": { \"houseNumber\": \"400\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$").isArray()).andExpect(status().isBadRequest()).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } }, { \"id\": \"1002\", \"firstName\": \"Charles\", \"lastName\": \"Bailey\", \"age\": 28, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"420\", \"city\": \"Utrecht\", \"zipCode\": \"2928ZP\" } }, { \"id\": \"1003\", \"firstName\": \"Chloe\", \"lastName\": \"Forsyth\", \"age\": 25, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"103a\", \"city\": \"De Hauge\", \"zipCode\": \"3928ZP\" } }, { \"id\": \"1004\", \"firstName\": \"Selman\", \"lastName\": \"Schriemer\", \"age\": 62, \"currentLivingAddress\": { \"street\": \"Kastanjestraat\", \"houseNumber\": \"137\", \"city\": \"Schagen\", \"zipCode\": \"1741WK\" } }, { \"id\": \"1005\", \"firstName\": \"Rik\", \"lastName\": \"Rik\", \"age\": 32, \"currentLivingAddress\": { \"street\": \"Postweg\", \"houseNumber\": \"81\", \"city\": \"Buren\", \"zipCode\": \"9164LS\" } } ]"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/customers/1001")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "{ \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" }}"));

        mockMvc.perform(get("/customers/2001")).andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Customer with Id 2001 not found"))).andExpect(jsonPath("$.timestamp", is(notNullValue())));

    }

    @Test
    void testGetCustomerByName() throws Exception {
        mockMvc.perform(get("/customers/search?firstName=john")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
        mockMvc.perform(get("/customers/search?lastName=doE")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
        mockMvc.perform(get("/customers/search?firstName=john&lastName=doE")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[ { \"id\": \"1001\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 20, \"currentLivingAddress\": { \"street\": \"SOMESTREET\", \"houseNumber\": \"200\", \"city\": \"Amsterdam\", \"zipCode\": \"1928ZP\" } } ]"));
    }

    @Test
    void testUpdateCustomerAddress() throws Exception {
        mockMvc.perform(patch("/customers/1003").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"street\": \"SOMESTREESAT\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }")).andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/customers/2001").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"street\": \"SOMESTREESAT\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }")).andDo(print())
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Customer with Id 2001 not found"))).andExpect(jsonPath("$.timestamp", is(notNullValue())));

        mockMvc.perform(patch("/customers/1003").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"street\": \"\", \"houseNumber\": \"4200\", \"city\": \"Amsterdam\", \"zipCode\": \"1980ST\" }")).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.[0].message", is("Street name is mandatory for address")));
    }

}
