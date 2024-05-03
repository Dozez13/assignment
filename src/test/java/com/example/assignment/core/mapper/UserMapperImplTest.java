package com.example.assignment.core.mapper;

import com.example.assignment.config.AbstractUnitServiceTest;
import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperImplTest extends AbstractUnitServiceTest {
    private UserMapperImpl userMapperImpl = new UserMapperImpl();

    @Test
    void shouldMapUserToUserGetDto() {
        //GIVEN
        Long userId = 1L;
        User user = createUser(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserGetDto expectedUserGetDto = createUserGetDto(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        //WHEN
        UserGetDto actualUserGetDto = userMapperImpl.toUserGetDto(user);
        //THEN
        assertEquals(expectedUserGetDto, actualUserGetDto);

    }

    @Test
    void shouldNotMapUserToUserGetDtoIfNull() {
        //GIVEN
        //WHEN
        UserGetDto actualUserGetDto = userMapperImpl.toUserGetDto(null);
        //THEN
        assertNull(actualUserGetDto);

    }

    @Test
    void shouldMapUserPostDtoToUser() {
        //GIVEN
        UserPostPutDto userPostPutDto = createUserPostPutDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User expectedUser = createUser(null, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        //WHEN
        User actualUser = userMapperImpl.fromUserPostPutDto(userPostPutDto);
        //THEN
        assertEquals(expectedUser, actualUser);

    }

    @Test
    void shouldNotMapUserPostDtoToUser() {
        //GIVEN
        //WHEN
        User actualUser = userMapperImpl.fromUserPostPutDto(null);
        //THEN
        assertNull(actualUser);

    }

    @Test
    void shouldUpdateUserFromUserPutDto() {
        //GIVEN
        Long userId = 1L;
        UserPostPutDto userPostPutDto = createUserPostPutDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User updatedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        User expectedUser = createUser(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        //WHEN
        userMapperImpl.update(userPostPutDto, updatedUser);
        //THEN
        assertEquals(expectedUser, updatedUser);
    }

    @Test
    void shouldNotUpdateUserFromUserPutDto() {
        //GIVEN
        Long userId = 1L;
        User updatedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        User expectedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        //WHEN
        userMapperImpl.update(null, updatedUser);
        //THEN
        assertEquals(expectedUser, updatedUser);
    }

    @Test
    void shouldPatchUserFromUserPatchDto() {
        //GIVEN
        Long userId = 1L;
        UserPatchDto userPatchDto = createUserPatchDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User updatedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        User expectedUser = createUser(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        //WHEN
        userMapperImpl.patch(userPatchDto, updatedUser);
        //THEN
        assertEquals(expectedUser, updatedUser);
    }

    @Test
    void shouldNotPatchUserFromUserPatchDtoUndefinedFields() {
        //GIVEN
        Long userId = 1L;
        UserPatchDto userPatchDto = createUserPatchDto(JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined());
        User updatedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        User expectedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        //WHEN
        userMapperImpl.patch(userPatchDto, updatedUser);
        //THEN
        assertEquals(expectedUser, updatedUser);
    }
    @Test
    void shouldNotPatchUserFromUserPatchDto() {
        //GIVEN
        Long userId = 1L;
        User updatedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        User expectedUser = createUser(userId, "email2", "firstName2", "lastName2", LocalDate.now().plusDays(1), "address2", "phoneNumber2");
        //WHEN
        userMapperImpl.patch(null, updatedUser);
        //THEN
        assertEquals(expectedUser, updatedUser);
    }


}
