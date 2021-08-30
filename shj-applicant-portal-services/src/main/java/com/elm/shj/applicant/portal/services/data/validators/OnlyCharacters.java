/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.data.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation annotation to validate that a value has only characters
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = OnlyCharactersValidator.class)
@Documented
public @interface OnlyCharacters {

    String message() default "validation.data.constraints.msg.20001";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return if characters should only be arabic
     */
    boolean arabic() default false;

    /**
     * @return if to allow empty values
     */
    boolean allowEmpty() default true;

    /**
     * @return if to allow number values
     */
    boolean allowNumbers() default false;

    /**
     * @return if to allow special characters (-_./)
     */
    boolean allowSpecialChars() default false;

    /**
     * @return The min field length
     */
    int min();

    /**
     * @return The max field length
     */
    int max();

}
