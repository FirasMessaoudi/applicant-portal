package com.elm.shj.applicant.portal.services.data.validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HajjPasswordValidator implements ConstraintValidator<HajjPassword, Object> {
    private static final String EML_PASSWORD_REGEXP = "^(?=.{8,}$)(?=.*[A-Z]*[A-Z])(?=.*[a-z]*[a-z])(?=.*[0-9]*[0-9])(?:([\\S*?!:;])\\1?(?!\\1))+$";

    public HajjPasswordValidator() {
    }

    public void initialize(HajjPassword constraintAnnotation) {
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null && value.toString().matches(EML_PASSWORD_REGEXP);
    }
}
