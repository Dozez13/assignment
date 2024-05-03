package com.example.assignment.domain.repository.filter.builder;

import com.querydsl.core.types.Predicate;

import java.time.LocalDate;

public interface UserFilterPredicateBuilder {
    Predicate build();

    UserFilterPredicateBuilder email(String email);

    UserFilterPredicateBuilder firstName(String firstName);

    UserFilterPredicateBuilder lastName(String lastName);

    UserFilterPredicateBuilder fromBirthDate(LocalDate fromBirthDate);
    UserFilterPredicateBuilder toBirthDate(LocalDate fromBirthDate);
    UserFilterPredicateBuilder address(String address);
    UserFilterPredicateBuilder phoneNumber(String phoneNumber);
}
