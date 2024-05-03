package com.example.assignment.domain.repository.filter.builder.impl;

import com.example.assignment.domain.entity.QUser;
import com.example.assignment.domain.repository.filter.builder.UserFilterPredicateBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserFilterPredicateBuilderImpl implements UserFilterPredicateBuilder {
    private final QUser qUser = QUser.user;
    private final BooleanBuilder booleanBuilder = new BooleanBuilder(qUser.isNotNull());

    @Override
    public Predicate build() {
        return booleanBuilder;
    }

    @Override
    public UserFilterPredicateBuilder email(String email) {
        if(Objects.nonNull(email)){
            booleanBuilder.and(qUser.email.eq(email));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder firstName(String firstName) {
        if(Objects.nonNull(firstName)){
            booleanBuilder.and(qUser.firstName.eq(firstName));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder lastName(String lastName) {
        if(Objects.nonNull(lastName)){
            booleanBuilder.and(qUser.lastName.eq(lastName));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder fromBirthDate(LocalDate fromBirthDate) {
        if(Objects.nonNull(fromBirthDate)){
            booleanBuilder.and(qUser.birthDate.gt(fromBirthDate));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder toBirthDate(LocalDate fromBirthDate) {
        if(Objects.nonNull(fromBirthDate)){
            booleanBuilder.and(qUser.birthDate.lt(fromBirthDate));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder address(String address) {
        if(Objects.nonNull(address)){
            booleanBuilder.and(qUser.address.eq(address));
        }
        return this;
    }

    @Override
    public UserFilterPredicateBuilder phoneNumber(String phoneNumber) {
        if(Objects.nonNull(phoneNumber)){
            booleanBuilder.and(qUser.phoneNumber.eq(phoneNumber));
        }
        return this;
    }
}
