package com.example.assignment.domain.repository.param;


import com.example.assignment.core.constraint.DateRange;
import com.example.assignment.domain.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Path;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiObject(name = "UserGetDto")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@DateRange
public class UserSearchParameters {
    @ApiObjectField(description = "User email")
    private String email;
    @ApiObjectField(description = "User first name")
    private String firstName;
    @ApiObjectField(description = "User last name")
    private String lastName;
    @ApiObjectField(description = "User address")
    private String address;
    @ApiObjectField(description = "User phone number")
    private String phoneNumber;
    @ApiObjectField(description = "User search start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @ApiObjectField(description = "User search end date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    @ApiObjectField(description = "User search order direction")
    private Order sortDirection;
    @ApiObjectField(description = "User search sort parameters")
    private SortByParameters sortBy;

    @RequiredArgsConstructor
    @Getter
    public enum SortByParameters {

        EMAIL(QUser.user.email),
        FIRST_NAME(QUser.user.firstName),
        LAST_NAME(QUser.user.lastName),
        ADDRESS(QUser.user.address),
        PHONE_NUMBER(QUser.user.phoneNumber),
        BIRTH_DATE(QUser.user.birthDate);

        private final Path<? extends Comparable<?>> orderPath;


    }


}

