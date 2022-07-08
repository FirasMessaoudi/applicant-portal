package com.elm.shj.applicant.portal.services.data.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {HajjPasswordValidator.class}
)
@Documented
public @interface HajjPassword {
    String message() default "{dcc.commons.validation.constraints.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
