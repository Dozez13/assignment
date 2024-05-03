package com.example.assignment.datagenerator.dto;

import com.example.assignment.core.dto.UserPostPutDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class UserPostPutDtoGenerator {
    public UserPostPutDto userPostPutDto() {
        UserPostPutDto userPostPutDto = new UserPostPutDto();
        userPostPutDto.setEmail("email");
        userPostPutDto.setFirstName("firstName");
        userPostPutDto.setLastName("lastName");
        userPostPutDto.setBirthDate(LocalDate.now());
        userPostPutDto.setAddress("address");
        userPostPutDto.setPhoneNumber("phoneNumber");
        return userPostPutDto;
    }

    public UserPostPutDto userPostPutDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
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
