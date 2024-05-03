package com.example.assignment.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.time.LocalDate;
@ApiObject(name = "UserGetDto")
@NoArgsConstructor
@Data
public class UserGetDto {
    @ApiObjectField(description = "User id")
    private Long id;
    @ApiObjectField(description = "User email")
    private String email;
    @ApiObjectField(description = "User first name")
    private String firstName;
    @ApiObjectField(description = "User last name")
    private String lastName;
    @ApiObjectField(description = "User birthdate")
    private LocalDate birthDate;
    @ApiObjectField(description = "User address")
    private String address;
    @ApiObjectField(description = "User phone number")
    private String phoneNumber;
}
