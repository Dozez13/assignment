package com.example.assignment.core.service;

import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.repository.param.UserSearchParameters;

import java.util.List;

public interface UserService {
    List<UserGetDto> getUserByBirthDateRange(UserSearchParameters userSearchParameters);

    void createUser(UserPostPutDto userPostPutDto);

    void updateUser(Long id, UserPostPutDto userPostPutDto);

    void patchUser(Long id, UserPatchDto userPatchDto);

    void deleteUser(Long id);
}
