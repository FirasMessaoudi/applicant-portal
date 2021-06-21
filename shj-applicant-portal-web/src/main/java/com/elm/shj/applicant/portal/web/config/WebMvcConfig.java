/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.config;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;

/**
 * Main web configuration
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String LANG_PARAM_NAME = "lang";
    private static final int DEFAULT_PAGE_SIZE = 10;


    /**
     * {@inheritDoc}
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(deviceHandlerMethodArgumentResolver());
        argumentResolvers.add(sitePreferenceHandlerMethodArgumentResolver());
        // adding paging config
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, DEFAULT_PAGE_SIZE));
        argumentResolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * Filter used to get the client ip when running behind a load balance or having proxy
     *
     * @return remoteIpFilter bean
     */
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    /**
     * Creates a bundle instance
     *
     * @return messageSource bean
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    /**
     * Creates a DeviceHandlerMethodArgumentResolver instance
     *
     * @return DeviceHandlerMethodArgumentResolver bean
     */
    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    /**
     * Creates a SitePreferenceHandlerMethodArgumentResolver instance
     *
     * @return SitePreferenceHandlerMethodArgumentResolver bean
     */
    @Bean
    public SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver() {
        return new SitePreferenceHandlerMethodArgumentResolver();
    }

    /**
     * Creates a DeviceResolverHandlerInterceptor instance
     *
     * @return DeviceResolverHandlerInterceptor bean
     */
    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    /**
     * Creates a property placeholder instance
     *
     * @return propertyPlaceholder bean
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }

    /**
     * Creates a validator instance
     *
     * @return localValidator bean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    /**
     * Creates a MethodValidationPostProcessor instance to enable @Autowired in ConstraintValidator
     *
     * @return MethodValidationPostProcessor bean
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(LANG_PARAM_NAME);
        localeChangeInterceptor.setIgnoreInvalidLocale(true);
        registry.addInterceptor(localeChangeInterceptor);

        registry.addInterceptor(deviceResolverHandlerInterceptor());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper pathHelper = new UrlPathHelper();
        pathHelper.setAlwaysUseFullPath(true);
        configurer.setUrlPathHelper(pathHelper);
    }
}
