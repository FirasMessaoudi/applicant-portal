/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Main persistence configuration
 * 
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@Profile("!test")
@EnableJpaRepositories(basePackages = "com.elm.shj.applicant.portal.orm.repository")
public class JpaConfig {

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired Environment env, @Autowired DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPersistenceUnitName("default-pu");
		em.setPackagesToScan("com.elm.shj.applicant.portal.orm.entity");
		em.setJpaVendorAdapter(hibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties(env));

		return em;
	}

	@Bean
	public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public Properties hibernateProperties(Environment env) {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
		properties.put("hibernate.format_sql", env.getProperty("spring.jpa.format-sql"));
		properties.put("hibernate.id.new_generator_mappings", "false");
		properties.put("hibernate.default_schema", env.getProperty("spring.jpa.default_schema"));
		return properties;
	}
}
