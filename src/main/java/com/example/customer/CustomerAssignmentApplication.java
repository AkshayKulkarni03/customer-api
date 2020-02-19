package com.example.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
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
