package com.example.assignment.domain.repository;

import com.example.assignment.config.AbstractUnitDBTest;
import com.example.assignment.domain.entity.User;
import com.example.assignment.domain.repository.filter.builder.impl.UserFilterPredicateBuilderImpl;
import com.example.assignment.domain.repository.impl.CustomizedUserRepositoryImpl;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(value = {CustomizedUserRepositoryImpl.class, UserFilterPredicateBuilderImpl.class})
public class CustomizedUserRepositoryImplTest extends AbstractUnitDBTest {

    @Autowired
    private CustomizedUserRepository customizedUserRepository;


    @Test
    @DatabaseSetup("classpath:datasets/users.xml")
    void shouldFindUsersBetweenDates() {
        //GIVEN
        LocalDate fromDate = LocalDate.of(2000, 3, 20);
        LocalDate toDate = LocalDate.of(2004, 5, 20);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .from(fromDate)
                .to(toDate)
                .build();
        //WHEN
        List<User> fetchedUsers = customizedUserRepository.retrieveUsersByBirthDateRange(userSearchParameters);
        //THEN
        assertEquals(2, fetchedUsers.size());
    }

    @Test
    void shouldNotFindUsersBetweenDates() {
        //GIVEN
        LocalDate fromDate = LocalDate.of(2000, 3, 20);
        LocalDate toDate = LocalDate.of(2004, 5, 20);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .from(fromDate)
                .to(toDate)
                .build();
        //WHEN
        List<User> fetchedUsers = customizedUserRepository.retrieveUsersByBirthDateRange(userSearchParameters);
        //THEN
        assertTrue(fetchedUsers.isEmpty());
    }

}
