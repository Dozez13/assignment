package com.example.assignment.domain.repository.impl;

import com.example.assignment.domain.entity.QUser;
import com.example.assignment.domain.entity.User;
import com.example.assignment.domain.repository.CustomizedUserRepository;
import com.example.assignment.domain.repository.filter.builder.UserFilterPredicateBuilder;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    private final QUser qUser = QUser.user;
    private final Order defaultOrder = Order.ASC;
    private final Path<LocalDate> defaultOrderPath = qUser.birthDate;

    @PersistenceContext
    private EntityManager entityManager;

    @Lookup
    UserFilterPredicateBuilder getUserFilterPredicateBuilder() {
        return null;
    }

    @Override
    public List<User> retrieveUsersByBirthDateRange(UserSearchParameters userSearchParameters) {
        JPAQuery<QUser> jpaQuery = new JPAQuery<>(entityManager);
        return jpaQuery
                .select(qUser)
                .from(qUser)
                .where(getFilter(userSearchParameters))
                .orderBy(getOrderSpecifier(userSearchParameters))
                .fetch();
    }

    private Predicate getFilter(UserSearchParameters searchParameters) {
        return getUserFilterPredicateBuilder()
                .email(searchParameters.getEmail())
                .firstName(searchParameters.getFirstName())
                .lastName(searchParameters.getLastName())
                .fromBirthDate(searchParameters.getFrom())
                .toBirthDate(searchParameters.getTo())
                .address(searchParameters.getAddress())
                .phoneNumber(searchParameters.getPhoneNumber())
                .build();
    }

    private OrderSpecifier<?> getOrderSpecifier(UserSearchParameters userSearchParameters) {
        OrderSpecifier<?> orderSpecifier;
        if (Objects.isNull(userSearchParameters.getSortBy()) && Objects.isNull(userSearchParameters.getSortDirection())) {
            orderSpecifier = new OrderSpecifier<>(defaultOrder, defaultOrderPath);
        } else if (Objects.isNull(userSearchParameters.getSortBy())) {
            orderSpecifier = new OrderSpecifier<>(userSearchParameters.getSortDirection(), defaultOrderPath);
        } else if (Objects.isNull(userSearchParameters.getSortDirection())) {
            orderSpecifier = new OrderSpecifier<>(defaultOrder, userSearchParameters.getSortBy().getOrderPath());
        } else {
            orderSpecifier = new OrderSpecifier<>(userSearchParameters.getSortDirection(), userSearchParameters.getSortBy().getOrderPath());
        }
        return orderSpecifier;
    }
}
