package com.example.assignment.domain.repository;

import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.example.assignment.domain.entity.User;

import java.util.List;

public interface CustomizedUserRepository {
    List<User> retrieveUsersByBirthDateRange(UserSearchParameters userSearchParameters);
}
