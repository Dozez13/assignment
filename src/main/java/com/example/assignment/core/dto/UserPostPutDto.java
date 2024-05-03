package com.example.assignment.core.dto;

import com.example.assignment.core.constraint.ValidBirthDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiObject(name = "UserPostPutDto")
@NoArgsConstructor
@Data
public class UserPostPutDto {
    @ApiObjectField(description = "User email")
    @Email
    @NotNull
    private String email;
    @ApiObjectField(description = "User first name")
    @NotNull
    private String firstName;
    @ApiObjectField(description = "User last name")
    @NotNull
    private String lastName;
    @ApiObjectField(description = "User birthdate")
    @ValidBirthDate
    @NotNull
    private LocalDate birthDate;
    @ApiObjectField(description = "User address")
    private String address;
    @ApiObjectField(description = "User phone number")
    private String phoneNumber;
}