package com.example.assignment.core.validator;


import com.example.assignment.core.constraint.Adult;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private final Long adultEdgePoint;

    public AdultValidator(@Value("${adult.edge.point}") Long adultEdgePoint) {
        this.adultEdgePoint = adultEdgePoint;
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) {
            return true;
        }
        long personYears = ChronoUnit.YEARS.between(Year.from(localDate), Year.from(LocalDate.now()));
        return personYears > adultEdgePoint;
    }
}
