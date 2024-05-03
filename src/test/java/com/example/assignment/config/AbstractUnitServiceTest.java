package com.example.assignment.config;

import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.entity.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUnitServiceTest {
    protected User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBirthDate(LocalDate.now());
        user.setAddress("address");
        user.setPhoneNumber("phoneNumber");
        return user;
    }

    protected User createUser(Long id, String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
    protected UserGetDto createUserGetDto() {
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(1L);
        userGetDto.setEmail("email");
        userGetDto.setFirstName("firstName");
        userGetDto.setLastName("lastName");
        userGetDto.setBirthDate(LocalDate.now());
        userGetDto.setAddress("address");
        userGetDto.setPhoneNumber("phoneNumber");
        return userGetDto;
    }

    protected UserGetDto createUserGetDto(Long id, String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(id);
        userGetDto.setEmail(email);
        userGetDto.setFirstName(firstName);
        userGetDto.setLastName(lastName);
        userGetDto.setBirthDate(birthDate);
        userGetDto.setAddress(address);
        userGetDto.setPhoneNumber(phoneNumber);
        return userGetDto;
    }
    protected UserPatchDto createUserPatchDto() {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(JsonNullable.of("email"));
        userPatchDto.setFirstName(JsonNullable.of("firstName"));
        userPatchDto.setLastName(JsonNullable.of("lastName"));
        userPatchDto.setBirthDate(JsonNullable.of(LocalDate.now()));
        userPatchDto.setAddress(JsonNullable.of("address"));
        userPatchDto.setPhoneNumber(JsonNullable.of("phoneNumber"));
        return userPatchDto;
    }

    protected UserPatchDto createUserPatchDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(JsonNullable.of(email));
        userPatchDto.setFirstName(JsonNullable.of(firstName));
        userPatchDto.setLastName(JsonNullable.of(lastName));
        userPatchDto.setBirthDate(JsonNullable.of(birthDate));
        userPatchDto.setAddress(JsonNullable.of(address));
        userPatchDto.setPhoneNumber(JsonNullable.of(phoneNumber));
        return userPatchDto;
    }

    protected UserPatchDto createUserPatchDto(JsonNullable<String> email, JsonNullable<String> firstName, JsonNullable<String> lastName, JsonNullable<LocalDate> birthDate, JsonNullable<String> address, JsonNullable<String> phoneNumber) {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(email);
        userPatchDto.setFirstName(firstName);
        userPatchDto.setLastName(lastName);
        userPatchDto.setBirthDate(birthDate);
        userPatchDto.setAddress(address);
        userPatchDto.setPhoneNumber(phoneNumber);
        return userPatchDto;
    }
    protected UserPostPutDto createUserPostPutDto() {
        UserPostPutDto userPostPutDto = new UserPostPutDto();
        userPostPutDto.setEmail("email");
        userPostPutDto.setFirstName("firstName");
        userPostPutDto.setLastName("lastName");
        userPostPutDto.setBirthDate(LocalDate.now());
        userPostPutDto.setAddress("address");
        userPostPutDto.setPhoneNumber("phoneNumber");
        return userPostPutDto;
    }

    protected UserPostPutDto createUserPostPutDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        UserPostPutDto userPostPutDto = new UserPostPutDto();
        userPostPutDto.setEmail(email);
        userPostPutDto.setFirstName(firstName);
        userPostPutDto.setLastName(lastName);
        userPostPutDto.setBirthDate(birthDate);
        userPostPutDto.setAddress(address);
        userPostPutDto.setPhoneNumber(phoneNumber);
        return userPostPutDto;
    }
}
