package com.example.assignment.web.controller;

import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.example.assignment.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(name = "User API", description = "Provides a list of methods that can be used to manage users")
@RestController
@RequestMapping("/api/v1/secure/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiMethod(description = "Get users by search parameters(email, firstName, lastName, birthDate, address, phoneNumber")
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "Validation failed, from birthdate should be before to birthdate and to birthdate should be after from birthdate"),
    })
    @ApiResponseObject
    @GetMapping
    public ResponseEntity<?> getAllUsers(@ApiQueryParam @Valid UserSearchParameters userSearchParameters) {
        return new ResponseEntity<>(userService.getUserByBirthDateRange(userSearchParameters), HttpStatus.OK);
    }

    @ApiMethod(description = "Create user")
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "Validation failed, params should be valid"),
    })
    @ApiResponseObject
    @PostMapping
    public ResponseEntity<?> createUser(@ApiBodyObject @Valid @RequestBody UserPostPutDto userPostPutDto) {
        userService.createUser(userPostPutDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @ApiMethod(description = "Update user")
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "Validation failed, params should be valid or user not found"),
    })
    @ApiResponseObject
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@ApiPathParam(name = "id") @PathVariable Long id, @ApiBodyObject @Valid @RequestBody UserPostPutDto userPostPutDto) {
        userService.updateUser(id, userPostPutDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @ApiMethod(description = "Patch user")
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "Validation failed, params should be valid or user not found"),
    })
    @ApiResponseObject
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@ApiPathParam(name = "id") @PathVariable Long id, @ApiBodyObject @Valid @RequestBody UserPatchDto userPatchDto) {
        userService.patchUser(id, userPatchDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @ApiMethod(description = "Delete user")
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "User not found"),
    })
    @ApiResponseObject
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@ApiPathParam(name = "id") @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
