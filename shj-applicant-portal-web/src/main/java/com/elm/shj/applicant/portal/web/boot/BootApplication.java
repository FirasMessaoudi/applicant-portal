/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.boot;


import com.elm.dcc.foundation.commons.web.xss.XssFilter;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletContext;

/**
 * Main bootable class
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.elm.shj.applicant.portal.web", "com.elm.dcc.foundation.providers", "com.elm.dcc.foundation.commons.web"})
public class BootApplication extends SpringBootServletInitializer {

    private static Class<BootApplication> bootClass = BootApplication.class;

    /**
     * This is used when running Spring Boot as Executable JAR
     *
     * @param args auto injected arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(bootClass, args);
    }

    /**
     * This is used when running Spring Boot as classic WAR
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(bootClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        servletContext.addListener(new RequestContextListener());

        servletContext.addFilter("xss-filter", new XssFilter()).addMappingForUrlPatterns(null, false, "/*");
        servletContext.addFilter("xframe-filter", new HttpHeaderSecurityFilter()).addMappingForUrlPatterns(null, false, "/*");
        servletContext.addFilter("device-filter", new DeviceResolverRequestFilter()).addMappingForUrlPatterns(null, false, "/*");
        servletContext.addFilter("charset-filter", new CharacterEncodingFilter("UTF-8", true)).addMappingForUrlPatterns(null, false, "/*");

        return super.createRootApplicationContext(servletContext);
    }

}
