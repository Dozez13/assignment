package com.example.assignment.core.constraint;

import com.example.assignment.core.validator.AdultValidator;

import java.lang.annotation.ElementType;

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@javax.validation.Constraint(validatedBy = {AdultValidator.class})
public @interface Adult {
    java.lang.String message() default "{com.example.assignment.core.constraint.Adult.message}";

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends javax.validation.Payload>[] payload() default {};

}
