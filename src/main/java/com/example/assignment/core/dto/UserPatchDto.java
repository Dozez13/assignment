package com.example.assignment.core.dto;


import com.example.assignment.core.constraint.ValidBirthDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@ApiObject(name = "UserPatchDto")
@NoArgsConstructor
@Data
public class UserPatchDto {
    @ApiObjectField(description = "User email")
    @Email
    @NotNull
    private JsonNullable<String> email = JsonNullable.undefined();
    @ApiObjectField(description = "User first name")
    @NotNull
    private JsonNullable<String> firstName = JsonNullable.undefined();
    @ApiObjectField(description = "User last name")
    @NotNull
    private JsonNullable<String> lastName = JsonNullable.undefined();
    @ApiObjectField(description = "User birthdate")
    @ValidBirthDate
    @NotNull
    private JsonNullable<LocalDate> birthDate = JsonNullable.undefined();
    @ApiObjectField(description = "User address")
    private JsonNullable<String> address = JsonNullable.undefined();
    @ApiObjectField(description = "User phone number")
    private JsonNullable<String> phoneNumber = JsonNullable.undefined();
}
