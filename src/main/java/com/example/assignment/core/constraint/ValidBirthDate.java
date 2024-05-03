package com.example.assignment.core.constraint;

import com.example.assignment.core.validator.AdultValidator;

import javax.validation.constraints.Past;

@Adult
@Past
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@javax.validation.Constraint(validatedBy = {})
public @interface ValidBirthDate {
    java.lang.String message() default "{com.example.assignment.core.constraint.Adult.message}";

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends javax.validation.Payload>[] payload() default {};

}