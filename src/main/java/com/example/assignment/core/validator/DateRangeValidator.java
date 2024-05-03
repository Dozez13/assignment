package com.example.assignment.core.validator;

import com.example.assignment.core.constraint.DateRange;
import com.example.assignment.domain.repository.param.UserSearchParameters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;

public class DateRangeValidator implements ConstraintValidator<DateRange, UserSearchParameters> {
    @Override
    public boolean isValid(UserSearchParameters userSearchParameters, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(userSearchParameters) || Objects.isNull(userSearchParameters.getFrom()) || Objects.isNull(userSearchParameters.getTo())) {
            return true;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("{com.example.assignment.core.constraint.DateRange.from.message}")
                .addPropertyNode("from").addConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("{com.example.assignment.core.constraint.DateRange.to.message}")
                .addPropertyNode("to").addConstraintViolation();
        LocalDate fromBirthDate = userSearchParameters.getFrom();
        LocalDate toBirthDate = userSearchParameters.getTo();
        return fromBirthDate.isBefore(toBirthDate);
    }
}
