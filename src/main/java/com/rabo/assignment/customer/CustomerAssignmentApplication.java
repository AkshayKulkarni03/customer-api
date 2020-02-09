package com.rabo.assignment.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.rabo.assignment.customer" })
@EnableMongoRepositories
public class CustomerAssignmentApplication {

	/**
	 * Main spring boot starter method to initialize and start application
	 * 
	 * @param args command line arguments if any required.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CustomerAssignmentApplication.class, args);
	}

}
