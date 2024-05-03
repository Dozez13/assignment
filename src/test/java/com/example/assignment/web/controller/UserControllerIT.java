package com.example.assignment.web.controller;

import com.example.assignment.config.GeneralTestConfig;
import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.core.exception.response.ErrorResponse;
import com.example.assignment.core.exception.response.FieldDetail;
import com.example.assignment.datagenerator.dto.UserGetDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPatchDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPostPutDtoGenerator;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({GeneralTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
})
@DatabaseSetup("classpath:datasets/user-reset.xml")
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserGetDtoGenerator userGetDtoGenerator;
    @Autowired
    private UserPostPutDtoGenerator userPostPutDtoGenerator;
    @Autowired
    private UserPatchDtoGenerator userPatchDtoGenerator;

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @DatabaseSetup("classpath:datasets/users.xml")
    void shouldGetReturnListOfUsers() {
        //GIVEN
        String path = "/api/v1/secure/users?from={from}&to={to}";
        String fromDate = "2000-03-20";
        String toDate = "2004-05-20";
        //WHEN
        ResponseEntity<List<UserGetDto>> userGetDtos = testRestTemplate.exchange(path, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<UserGetDto>>() {
        }, fromDate, toDate);
        //THEN
        assertEquals(2, userGetDtos.getBody().size());
    }

    @Test
    void shouldGetReturnEmptyListOfUsers() {
        //GIVEN
        String path = "/api/v1/secure/users?from={from}&to={to}";
        String fromDate = "2000-03-20";
        String toDate = "2004-05-20";
        //WHEN
        ResponseEntity<List<UserGetDto>> userGetDtos = testRestTemplate.exchange(path, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<UserGetDto>>() {
        }, fromDate, toDate);
        //THEN
        assertTrue(userGetDtos.getBody().isEmpty());
    }

    @Test
    void shouldGetReturnValidationErrorIfFromIsBiggerThatTo() {
        //WHEN
        String path = "/api/v1/secure/users?from={from}&to={to}";
        String fromDate = "2005-03-20";
        String toDate = "2004-05-20";
        //WHEN
        ErrorResponse<List<FieldDetail>> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", Arrays.asList(new FieldDetail("from", "from birthdate should be before to birthdate"), new FieldDetail("to", "to birthdate should be after from birthdate")));
        ResponseEntity<ErrorResponse<List<FieldDetail>>> actualErrorResponse = testRestTemplate.exchange(path, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ErrorResponse<List<FieldDetail>>>() {
        }, fromDate, toDate);
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse.getBody());
    }

    @Test
    void shouldPostReturnCreateIfBodyIsValid() {
        //GIVEN
        String path = "/api/v1/secure/users";
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        HttpEntity<UserPostPutDto> userPostPutDtoHttpEntity = new HttpEntity<>(userPostPutDto);
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.exchange(path, HttpMethod.POST, userPostPutDtoHttpEntity, Void.class);
        //THEN
        assertEquals(201, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource(value = {"postPutValidationErrors"})
    void shouldPostReturnValidationError(UserPostPutDto userPostPutDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        //GIVEN
        String path = "/api/v1/secure/users";
        HttpEntity<UserPostPutDto> userPostPutDtoHttpEntity = new HttpEntity<>(userPostPutDto);
        //WHEN
        ResponseEntity<ErrorResponse<List<FieldDetail>>> actualErrorResponse = testRestTemplate.exchange(path, HttpMethod.POST, userPostPutDtoHttpEntity, new ParameterizedTypeReference<ErrorResponse<List<FieldDetail>>>() {
        });
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse.getBody());
    }

    @Test
    @DatabaseSetup("classpath:datasets/user.xml")
    void shouldPutReturnIsOkIfBodyIsValid() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        HttpEntity<UserPostPutDto> userPostPutDtoHttpEntity = new HttpEntity<>(userPostPutDto);
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.exchange(path, HttpMethod.PUT, userPostPutDtoHttpEntity, Void.class, userId);
        //THEN
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldPutThrowExceptionIfUserNotFound() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        UserPostPutDto userPostPutDto = userPostPutDtoGenerator.userPostPutDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        HttpEntity<UserPostPutDto> userPostPutDtoHttpEntity = new HttpEntity<>(userPostPutDto);
        //WHEN
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ResponseEntity<ErrorResponse<?>> response = testRestTemplate.exchange(path, HttpMethod.PUT, userPostPutDtoHttpEntity, new ParameterizedTypeReference<ErrorResponse<?>>() {
        }, userId);
        //THEN
        assertEquals(expectedErrorResponse, response.getBody());
    }

    @ParameterizedTest
    @MethodSource(value = {"putValidationErrors"})
    void shouldPutReturnValidationError(Long userId, UserPostPutDto userPostPutDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        //GIVEN
        String path = "/api/v1/secure/users/{Id}";
        HttpEntity<UserPostPutDto> userPostPutDtoHttpEntity = new HttpEntity<>(userPostPutDto);
        //WHEN
        ResponseEntity<ErrorResponse<List<FieldDetail>>> actualErrorResponse = testRestTemplate.exchange(path, HttpMethod.PUT, userPostPutDtoHttpEntity, new ParameterizedTypeReference<ErrorResponse<List<FieldDetail>>>() {
        }, userId);
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse.getBody());
    }

    @Test
    @DatabaseSetup("classpath:datasets/user.xml")
    void shouldPatchReturnIsOkIfBodyIsValid() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        HttpEntity<UserPatchDto> userPatchDtoHttpEntity = new HttpEntity<>(userPatchDto);
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.exchange(path, HttpMethod.PATCH, userPatchDtoHttpEntity, Void.class, userId);
        //THEN
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DatabaseSetup("classpath:datasets/user.xml")
    void shouldPatchReturnIsOkIfBodyAttributesIsOmitted() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto(JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.undefined());
        Long userId = 1L;
        HttpEntity<UserPatchDto> userPatchDtoHttpEntity = new HttpEntity<>(userPatchDto);
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.exchange(path, HttpMethod.PATCH, userPatchDtoHttpEntity, Void.class, userId);
        //THEN
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldPatchThrowExceptionIfUserNotFound() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        UserPatchDto userPatchDto = userPatchDtoGenerator.userPatchDto("manuilenlopavel@gmail.com", "firstName", "lastName", LocalDate.of(2000, 5, 12), "address", "phoneNumber");
        Long userId = 1L;
        //WHEN
        HttpEntity<UserPatchDto> userPatchDtoHttpEntity = new HttpEntity<>(userPatchDto);
        //WHEN
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ResponseEntity<ErrorResponse<?>> response = testRestTemplate.exchange(path, HttpMethod.PATCH, userPatchDtoHttpEntity, new ParameterizedTypeReference<ErrorResponse<?>>() {
        }, userId);
        //THEN
        assertEquals(expectedErrorResponse, response.getBody());
    }

    @ParameterizedTest
    @MethodSource(value = {"patchValidationErrors"})
    void shouldPatchReturnValidationError(Long userId, UserPatchDto userPatchDto, ErrorResponse<List<FieldDetail>> expectedErrorResponse) throws Exception {
        String path = "/api/v1/secure/users/{id}";
        HttpEntity<UserPatchDto> userPatchDtoHttpEntity = new HttpEntity<>(userPatchDto);
        //WHEN
        ResponseEntity<ErrorResponse<List<FieldDetail>>> actualErrorResponse = testRestTemplate.exchange(path, HttpMethod.PATCH, userPatchDtoHttpEntity, new ParameterizedTypeReference<ErrorResponse<List<FieldDetail>>>() {
        }, userId);
        //THEN
        assertEquals(expectedErrorResponse, actualErrorResponse.getBody());
    }

    @Test
    @DatabaseSetup("classpath:datasets/user.xml")
    void shouldDeleteReturnIsOkIfBodyIsValid() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        Long userId = 1L;
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, userId);
        //THEN
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteThrowExceptionIfUserNotFound() {
        //GIVEN
        String path = "/api/v1/secure/users/{id}";
        Long userId = 1L;
        //WHEN
        ErrorResponse<?> expectedErrorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "User with such id does not exist", null);
        ResponseEntity<ErrorResponse<?>> response = testRestTemplate.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, new ParameterizedTypeReference<ErrorResponse<?>>() {
        }, userId);
        //THEN
        assertEquals(expectedErrorResponse, response.getBody());
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
