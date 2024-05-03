package com.example.assignment.web.controller;

import com.example.assignment.config.AbstractUnitWebTest;
import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.core.exception.UserNotFoundException;
import com.example.assignment.core.exception.response.ErrorResponse;
import com.example.assignment.core.exception.response.FieldDetail;
import com.example.assignment.datagenerator.dto.UserGetDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPatchDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPostPutDtoGenerator;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractUnitWebTest {
    @Autowired
    private UserGetDtoGenerator userGetDtoGenerator;
    @Autowired
    private UserPostPutDtoGenerator userPostPutDtoGenerator;
    @Autowired
    private UserPatchDtoGenerator userPatchDtoGenerator;

    @Test
    void shouldGetReturnListOfUsersIfParamsAreValid() throws Exception {
        //GIVEN
        LocalDate from = LocalDate.of(2000, 5, 12);
        LocalDate to = LocalDate.of(2002, 5, 12);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .from(from)
                .to(to)
                .build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        UserGetDto firstUserGetDto = userGetDtoGenerator.userGetDto(1L, "email", "firstName", "lastName", from, "address", "phoneNumber");
        UserGetDto secondUserGetDto = userGetDtoGenerator.userGetDto(2L, "email2", "firstName2", "lastName2", from, "address2", "phoneNumber2");
        List<UserGetDto> userGetDtos = Arrays.asList(firstUserGetDto, secondUserGetDto);
        //WHEN
        when(userService.getUserByBirthDateRange(userSearchParameters)).thenReturn(userGetDtos);
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/secure/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.format(dateTimeFormatter))
                        .param("to", to.format(dateTimeFormatter)))
                .andExpect(status().isOk())
                .andReturn();
        List<UserGetDto> actualUserGetDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<UserGetDto>>() {
        });
        //THEN
        verify(userService, times(1)).getUserByBirthDateRange(userSearchParameters);
        assertEquals(userGetDtos.size(), actualUserGetDtos.size());
        assertEquals(userGetDtos, actualUserGetDtos);
    }

    @Test
    void shouldGetReturnValidationError() throws Exception {
        //Given
        LocalDate from = LocalDate.of(2003, 5, 12);
        LocalDate to = LocalDate.of(2002, 5, 12);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .from(from)
                .to(to)
                .build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //WHEN
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/secure/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.format(dateTimeFormatter))
                        .param("to", to.format(dateTimeFormatter)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<List<FieldDetail>> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", Arrays.asList(new FieldDetail("from", "from birthdate should be before to birthdate"), new FieldDetail("to", "to birthdate should be after from birthdate")));
        ErrorResponse<List<FieldDetail>> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<List<FieldDetail>>>() {
        });
        //THEN
        verify(userService, never()).getUserByBirthDateRange(userSearchParameters);
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void shouldPostReturnCreateIfBodyIsValid() throws Exception {
        //GIVEN
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        ArgumentCaptor<UserPostPutDto> userCaptor = ArgumentCaptor.forClass(UserPostPutDto.class);
        //WHEN
        mockMvc.perform(post("/api/v1/secure/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isCreated());
        //THEN
        verify(userService).createUser(userCaptor.capture());
        assertEquals(userPostPutDto, userCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource(value = {"postPutValidationErrors"})
    void shouldPostReturnValidationError(UserPostPutDto userPostPutDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        //GIVEN
        //WHEN
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/secure/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<List<FieldDetail>> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<List<FieldDetail>>>() {
        });
        //THEN
        verify(userService, never()).createUser(userPostPutDto);
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void shouldPutReturnIsOkIfBodyIsValid() throws Exception {
        //GIVEN
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        ArgumentCaptor<UserPostPutDto> userCaptor = ArgumentCaptor.forClass(UserPostPutDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        //WHEN
        mockMvc.perform(put("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isOk());
        //THEN
        verify(userService).updateUser(userIdCaptor.capture(), userCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(userPostPutDto, userCaptor.getValue());
    }

    @Test
    void shouldPutThrowExceptionIfUserNotFound() throws Exception {
        //GIVEN
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        //WHEN
        doThrow(new UserNotFoundException("User with such id does not exist")).when(userService).updateUser(userId, userPostPutDto);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ErrorResponse<?> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<?>>() {
        });
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @ParameterizedTest
    @MethodSource(value = {"putValidationErrors"})
    void shouldPutReturnValidationError(Long userId, UserPostPutDto userPostPutDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        //GIVEN
        //WHEN
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<List<FieldDetail>> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<List<FieldDetail>>>() {
        });
        //THEN
        verify(userService, never()).updateUser(userId, userPostPutDto);
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void shouldPatchReturnIsOkIfBodyIsValid() throws Exception {
        //GIVEN
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        ArgumentCaptor<UserPatchDto> userCaptor = ArgumentCaptor.forClass(UserPatchDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        //WHEN
        mockMvc.perform(patch("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isOk());
        //THEN
        verify(userService).patchUser(userIdCaptor.capture(), userCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(userPatchDto, userCaptor.getValue());
    }

    @Test
    void shouldPatchReturnIsOkIfBodyAttributesIsOmitted() throws Exception {
        //GIVEN
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto(JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined());
        Long userId = 1L;
        ArgumentCaptor<UserPatchDto> userCaptor = ArgumentCaptor.forClass(UserPatchDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        //WHEN
        mockMvc.perform(patch("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isOk());
        //THEN
        verify(userService).patchUser(userIdCaptor.capture(), userCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(userPatchDto, userCaptor.getValue());
    }

    @Test
    void shouldPatchThrowExceptionIfUserNotFound() throws Exception {
        //GIVEN
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        //WHEN
        doThrow(new UserNotFoundException("User with such id does not exist")).when(userService).patchUser(userId, userPatchDto);
        MvcResult mvcResult = mockMvc.perform(patch("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ErrorResponse<?> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<?>>() {
        });
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @ParameterizedTest
    @MethodSource(value = {"patchValidationErrors"})
    void shouldPatchReturnValidationError(Long userId, UserPatchDto userPatchDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        //GIVEN
        //WHEN
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<List<FieldDetail>> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<List<FieldDetail>>>() {
        });
        //THEN
        verify(userService, never()).patchUser(userId, userPatchDto);
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void shouldDeleteReturnIsOkIfBodyIsValid() throws Exception {
        //GIVEN
        Long userId = 1L;
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        //WHEN
        mockMvc.perform(delete("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        //THEN
        verify(userService).deleteUser(userIdCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
    }

    @Test
    void shouldDeleteThrowExceptionIfUserNotFound() throws Exception {
        //GIVEN
        Long userId = 1L;
        //WHEN
        doThrow(new UserNotFoundException("User with such id does not exist")).when(userService).deleteUser(userId);
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/secure/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ErrorResponse<?> actualErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorResponse<?>>() {
        });
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse);
    }


    private static Stream<Arguments> postPutValidationErrors() {
        return Stream.of(
                Arguments.of(
                        passedUserPostPut("manuilenlopavelgmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must be a well-formed email address"))),
                Arguments.of(passedUserPostPut(null, "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must not be null"))),
                Arguments.of(passedUserPostPut("manuilenlopavel@gmail.com", null, "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("firstName", "must not be null"))),
                Arguments.of(passedUserPostPut("manuilenlopavel@gmail.com", "firstName", null, LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("lastName", "must not be null"))),
                Arguments.of(passedUserPostPut("manuilenlopavel@gmail.com", "firstName", "lastName", null, "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"))),
                Arguments.of(passedUserPostPut("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.now().minusDays(1L), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "person should be adult"))),
                Arguments.of(passedUserPostPut("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must be a past date"), new FieldDetail("birthDate", "person should be adult"))),
                Arguments.of(passedUserPostPut(null, null, null, null, "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"), new FieldDetail("email", "must not be null"), new FieldDetail("firstName", "must not be null"), new FieldDetail("lastName", "must not be null")))
        );
    }

    private static Stream<Arguments> putValidationErrors() {
        return Stream.of(
                Arguments.of(
                        1L,
                        passedUserPostPut("manuilenlopavelgmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must be a well-formed email address"))),
                Arguments.of(
                        1L,
                        passedUserPostPut(null, "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPostPut("manuilenlopavel@gmail.com", null, "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("firstName", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPostPut("manuilenlopavel@gmail.com", "firstName", null, LocalDate.of(2000, 5, 12), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("lastName", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPostPut("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must be a past date"), new FieldDetail("birthDate", "person should be adult"))),
                Arguments.of(
                        1L,
                        passedUserPostPut("manuilenlopavel@gmail.com", "firstName", "lastName", null, "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPostPut(null, null, null, null, "address", "phoneNumber"),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"), new FieldDetail("email", "must not be null"), new FieldDetail("firstName", "must not be null"), new FieldDetail("lastName", "must not be null")))
        );
    }

    private static Stream<Arguments> patchValidationErrors() {
        return Stream.of(
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of("manuilenlopavelgmail.com"), JsonNullable.of("firstName"), JsonNullable.of("lastName"), JsonNullable.of(LocalDate.of(2000, 5, 12)), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must be a well-formed email address"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of(null), JsonNullable.of("firstName"), JsonNullable.of("lastName"), JsonNullable.of(LocalDate.of(2000, 5, 12)), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("email", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of("manuilenlopavel@gmail.com"), JsonNullable.of(null), JsonNullable.of("lastName"), JsonNullable.of(LocalDate.of(2000, 5, 12)), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("firstName", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of("manuilenlopavel@gmail.com"), JsonNullable.of("firstName"), JsonNullable.of(null), JsonNullable.of(LocalDate.of(2000, 5, 12)), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("lastName", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of("manuilenlopavel@gmail.com"), JsonNullable.of("firstName"), JsonNullable.of("lastName"), JsonNullable.of(LocalDate.now()), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must be a past date"), new FieldDetail("birthDate", "person should be adult"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of("manuilenlopavel@gmail.com"), JsonNullable.of("firstName"), JsonNullable.of("lastName"), JsonNullable.of(null), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"))),
                Arguments.of(
                        1L,
                        passedUserPatch(JsonNullable.of(null), JsonNullable.of(null), JsonNullable.of(null), JsonNullable.of(null), JsonNullable.of("address"), JsonNullable.of("phoneNumber")),
                        expectedErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", new FieldDetail("birthDate", "must not be null"), new FieldDetail("email", "must not be null"), new FieldDetail("firstName", "must not be null"), new FieldDetail("lastName", "must not be null")))
        );
    }

    private static ErrorResponse<List<FieldDetail>> expectedErrorResponse(HttpStatus status, String title, FieldDetail... fieldDetails) {
        return new ErrorResponse<>(status.value(), title, Arrays.asList(fieldDetails));
    }

    private static UserPostPutDto passedUserPostPut(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        UserPostPutDto userPostPutDto = new UserPostPutDto();
        userPostPutDto.setEmail(email);
        userPostPutDto.setFirstName(firstName);
        userPostPutDto.setLastName(lastName);
        userPostPutDto.setBirthDate(birthDate);
        userPostPutDto.setAddress(address);
        userPostPutDto.setPhoneNumber(phoneNumber);
        return userPostPutDto;
    }


    private static UserPatchDto passedUserPatch(JsonNullable<String> email, JsonNullable<String> firstName, JsonNullable<String> lastName, JsonNullable<LocalDate> birthDate, JsonNullable<String> address, JsonNullable<String> phoneNumber) {
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
