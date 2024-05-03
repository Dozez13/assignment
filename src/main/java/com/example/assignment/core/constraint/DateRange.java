package com.example.assignment.core.constraint;

import com.example.assignment.core.validator.DateRangeValidator;

import java.lang.annotation.ElementType;

@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@javax.validation.Constraint(validatedBy = {DateRangeValidator.class})
public @interface DateRange {
    java.lang.String message() default "{com.example.assignment.core.constraint.DateRange.message}";

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends javax.validation.Payload>[] payload() default {};

}