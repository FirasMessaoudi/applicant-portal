/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.config;

import org.apache.commons.configuration.DatabaseConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springmodules.commons.configuration.CommonsConfigurationFactoryBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * Main property source configuration
 *
 * @author Ahmad Flaifel
 * @since 1.3.0
 */
@Configuration
@EnableAutoConfiguration
public class PropertySourceConfig extends PropertySourcesPlaceholderConfigurer {

    private Environment env;

    /**
     * Initializes the configuration properties from the database
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        env = beanFactory.getBean(Environment.class);
        // skip this for tests
        if (Arrays.stream(env.getActiveProfiles()).anyMatch(e -> (e.equalsIgnoreCase("test")))) {
            return;
        }
        MutablePropertySources propertySources = ((ConfigurableEnvironment) env).getPropertySources();
        try {
            // dataSource, Table Name, Key Column, Value Column
            DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(dataSource(), getConfigTableFullName(),
                    env.getProperty("datasource.config.key.column.name"),
                    env.getProperty("datasource.config.value.column.name"));

            CommonsConfigurationFactoryBean commonsConfigurationFactoryBean = new CommonsConfigurationFactoryBean(
                    databaseConfiguration);

            Properties dbProps = (Properties) commonsConfigurationFactoryBean.getObject();
            PropertiesPropertySource dbPropertySource = new PropertiesPropertySource("dbPropertySource", dbProps);

            // By being First, Database Properties take precedence over all other properties that have the same key name
            propertySources.addFirst(dbPropertySource);
        } catch (Exception e) {
            logger.error("Error during database properties setup", e);
            throw new MissingRequiredPropertiesException();
        }
        super.postProcessBeanFactory(beanFactory);
    }

    @Bean("dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        ds.setUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        return ds;
    }

    private String getConfigTableFullName() {
        return String.join(".", env.getProperty("spring.jpa.default_schema"), env.getProperty("datasource.config" +
                ".table.name"));
    }
}
