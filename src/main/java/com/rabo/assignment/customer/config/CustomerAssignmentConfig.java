package com.rabo.assignment.customer.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

/**
 * Customer assignment configuration class for different application
 * configurations
 * 
 * @author akshay
 *
 */
@Configuration
public class CustomerAssignmentConfig {

	@Value("${mongo.db.url}")
	private String mongoDBUrl;

	@Value("${mongo.db.name}")
	private String mongoDBName;

	/**
	 * Initialization of in memory mongoDB database
	 * 
	 * @return mongoTemplate with basic configuration set.
	 * @throws IOException will be thrown in case of Mongo client creation failed.
	 */
	@SuppressWarnings("deprecation")
	public MongoTemplate mongoTemplate() throws IOException {
		EmbeddedMongoFactoryBean mongoFactoryBean = new EmbeddedMongoFactoryBean();
		mongoFactoryBean.setBindIp(mongoDBUrl);
		MongoClient mongoClient = mongoFactoryBean.getObject();
		return new MongoTemplate(mongoClient, mongoDBName);
	}
}
