package com.example.assignment.datagenerator.dto;

import com.example.assignment.core.dto.UserGetDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class UserGetDtoGenerator {
    public UserGetDto userGetDto() {
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

    public UserGetDto userGetDto(Long id, String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
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
}
