package com.example.assignment.datagenerator.dto;

import com.example.assignment.core.dto.UserPatchDto;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class UserPatchDtoGenerator {
    public UserPatchDto userPatchDto() {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(JsonNullable.of("email"));
        userPatchDto.setFirstName(JsonNullable.of("firstName"));
        userPatchDto.setLastName(JsonNullable.of("lastName"));
        userPatchDto.setBirthDate(JsonNullable.of(LocalDate.now()));
        userPatchDto.setAddress(JsonNullable.of("address"));
        userPatchDto.setPhoneNumber(JsonNullable.of("phoneNumber"));
        return userPatchDto;
    }

    public UserPatchDto userPatchDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(JsonNullable.of(email));
        userPatchDto.setFirstName(JsonNullable.of(firstName));
        userPatchDto.setLastName(JsonNullable.of(lastName));
        userPatchDto.setBirthDate(JsonNullable.of(birthDate));
        userPatchDto.setAddress(JsonNullable.of(address));
        userPatchDto.setPhoneNumber(JsonNullable.of(phoneNumber));
        return userPatchDto;
    }

    public UserPatchDto userPatchDto(JsonNullable<String> email, JsonNullable<String> firstName, JsonNullable<String> lastName, JsonNullable<LocalDate> birthDate, JsonNullable<String> address, JsonNullable<String> phoneNumber) {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setEmail(email);
        userPatchDto.setFirstName(firstName);
        userPatchDto.setLastName(lastName);
        userPatchDto.setBirthDate(birthDate);
        userPatchDto.setAddress(address);
        userPatchDto.setPhoneNumber(phoneNumber);
        return userPatchDto;
    }
}
