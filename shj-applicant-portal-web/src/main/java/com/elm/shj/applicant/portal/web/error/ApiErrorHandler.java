/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Global Error handler for all API calls
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    private static final String GENERAL_ERROR_KEY_NAME = "general error";
    private static final String GENERAL_ERROR_TEXT_KEY_NAME = "The server encountered an error processing the request. Please try again later.";
    private static final String UNIQUE_CONSTRAINT_VALIDATION_MSG_KEY = "dcc.commons.validation.constraints.unique";
    private static final String INVALID_CONSTRAINT_VALIDATION_MSG_KEY = "dcc.commons.validation.constraints.invalid";

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errorsMap = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            //replace function is used to fix security vulnerability
            String defaultMessage = error.getDefaultMessage();
            errorsMap.put(error.getField(), (defaultMessage == null) ? StringUtils.EMPTY : defaultMessage.replace(UNIQUE_CONSTRAINT_VALIDATION_MSG_KEY, INVALID_CONSTRAINT_VALIDATION_MSG_KEY));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorsMap.put(error.getObjectName(), error.getDefaultMessage());
        }

        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, GENERAL_ERROR_TEXT_KEY_NAME, errorsMap);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, ex.getParameterName() + " parameter is missing");

        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getRootBeanClass().getName() + " " +violation.getPropertyPath(), violation.getMessage());
        }

        ApiErrorResponse apiError =
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        Class<?> requiredType = ex.getRequiredType();
        errors.put(GENERAL_ERROR_KEY_NAME, ex.getName() + " should be of type " + ((requiredType == null) ? StringUtils.EMPTY : requiredType.getName()));

        ApiErrorResponse apiError =
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.NOT_FOUND, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        Set<HttpMethod> supportedHttpMethods = ex.getSupportedHttpMethods();
        if (supportedHttpMethods != null) {
            builder.append(" method is not supported for this request. Supported methods are ");
            supportedHttpMethods.forEach(t -> builder.append(t).append(", "));
        }
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, builder.toString());

        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, builder.substring(0, builder.length() - 2));

        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, GENERAL_ERROR_TEXT_KEY_NAME);
        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put(GENERAL_ERROR_KEY_NAME, GENERAL_ERROR_TEXT_KEY_NAME);
        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_ERROR_TEXT_KEY_NAME, errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
