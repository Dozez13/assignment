package com.example.assignment.core.service;

import com.example.assignment.config.AbstractUnitServiceTest;
import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.core.exception.UserNotFoundException;
import com.example.assignment.core.mapper.UserMapper;
import com.example.assignment.core.service.impl.UserServiceImpl;
import com.example.assignment.domain.entity.User;
import com.example.assignment.domain.repository.CustomizedUserRepository;
import com.example.assignment.domain.repository.UserRepository;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.querydsl.core.types.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserServiceImplTest extends AbstractUnitServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private MessageService messageService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomizedUserRepository customizedUserRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void shouldReturnUserGetDtos() {
        //GIVEN
        Long user1Id = 1L;
        Long user2Id = 1L;
        LocalDate fromBirthDate = LocalDate.of(2000, 5, 15);
        LocalDate toBirthDate = LocalDate.of(2000, 7, 25);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .sortBy(UserSearchParameters.SortByParameters.BIRTH_DATE)
                .sortDirection(Order.ASC)
                .from(fromBirthDate)
                .to(toBirthDate)
                .build();
        User user1 = createUser(user1Id, "email", "firstName", "lastName", fromBirthDate.plusDays(2), "address", "phoneNumber");
        User user2 = createUser(user2Id, "email2", "firstName2", "lastName2", fromBirthDate.plusDays(10), "address2", "phoneNumber2");
        List<User> users = Arrays.asList(user1, user2);
        UserGetDto expectedUserGetDto1 = createUserGetDto(user1Id, "email", "firstName", "lastName", fromBirthDate.plusDays(2), "address", "phoneNumber");
        UserGetDto expectedUserGetDto2 = createUserGetDto(user2Id, "email2", "firstName2", "lastName2", fromBirthDate.plusDays(10), "address2", "phoneNumber2");
        List<UserGetDto> expectedUserGetDtos = Arrays.asList(expectedUserGetDto1, expectedUserGetDto2);
        //WHEN
        when(customizedUserRepository.retrieveUsersByBirthDateRange(userSearchParameters))
                .thenReturn(users);
        when(userMapper.toUserGetDto(user1)).thenReturn(expectedUserGetDto1);
        when(userMapper.toUserGetDto(user2)).thenReturn(expectedUserGetDto2);
        List<UserGetDto> actualUserGetDtos = userServiceImpl.getUserByBirthDateRange(userSearchParameters);
        //THEN
        verify(customizedUserRepository, times(1)).retrieveUsersByBirthDateRange(userSearchParameters);
        verify(userMapper, times(1)).toUserGetDto(user1);
        verify(userMapper, times(1)).toUserGetDto(user2);
        assertEquals(expectedUserGetDtos.size(), actualUserGetDtos.size());
        assertEquals(expectedUserGetDtos, actualUserGetDtos);
    }

    @Test
    void shouldReturnEmptyListOfUserGetDtos() {
        //GIVEN
        LocalDate fromBirthDate = LocalDate.of(2000, 5, 15);
        LocalDate toBirthDate = LocalDate.of(2000, 7, 25);
        UserSearchParameters userSearchParameters = UserSearchParameters
                .builder()
                .from(fromBirthDate)
                .to(toBirthDate)
                .build();
        //WHEN
        when(customizedUserRepository.retrieveUsersByBirthDateRange(userSearchParameters))
                .thenReturn(Collections.emptyList());
        List<UserGetDto> actualUserGetDtos = userServiceImpl.getUserByBirthDateRange(userSearchParameters);
        //THEN
        assertTrue(actualUserGetDtos.isEmpty());
        verify(userMapper, never()).toUserGetDto(any());
    }

    @Test
    void shouldCreateUser() {
        //GIVEN
        UserPostPutDto userPostPutDto = createUserPostPutDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user = createUser(null, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        //WHEN
        when(userMapper.fromUserPostPutDto(userPostPutDto)).thenReturn(user);
        userServiceImpl.createUser(userPostPutDto);
        //THEN
        verify(userMapper, times(1)).fromUserPostPutDto(userPostPutDto);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateUserFromUserPutDto() {
        //GIVEN
        Long userId = 1L;
        UserPostPutDto userPostPutDto = createUserPostPutDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user = createUser(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UserPostPutDto> userPostPutDtoArgumentCaptor = ArgumentCaptor.forClass(UserPostPutDto.class);
        //WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userServiceImpl.updateUser(userId, userPostPutDto);
        //THEN
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper).update(userPostPutDtoArgumentCaptor.capture(), userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
        assertEquals(userPostPutDto, userPostPutDtoArgumentCaptor.getValue());
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserFromUserPutDto() {
        //GIVEN
        Long userId = 1L;
        UserPostPutDto userPostPutDto = createUserPostPutDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        String errorCode = "user.with.such.id.does.not.exist";
        String expectedErrorMessage = "User with such id does not exist";
        //WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(messageService.getMessage(errorCode)).thenReturn(expectedErrorMessage);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUser(userId, userPostPutDto));
        //THEN
        verify(userMapper, never()).update(eq(userPostPutDto), any());
        verify(userRepository, never()).save(any());
        verify(userRepository, times(1)).findById(userId);
        verify(messageService, times(1)).getMessage(errorCode);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void shouldPatchUserFromUserPatchDto() {
        //GIVEN
        Long userId = 1L;
        UserPatchDto userPatchDto = createUserPatchDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user = createUser(userId, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UserPatchDto> userPatchDtoArgumentCaptor = ArgumentCaptor.forClass(UserPatchDto.class);
        //WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userServiceImpl.patchUser(userId, userPatchDto);
        //THEN
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper).patch(userPatchDtoArgumentCaptor.capture(), userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
        assertEquals(userPatchDto, userPatchDtoArgumentCaptor.getValue());
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenPatchUserFromUserPatchDto() {
        //GIVEN
        Long userId = 1L;
        UserPatchDto userPatchDto = createUserPatchDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        String errorCode = "user.with.such.id.does.not.exist";
        String expectedErrorMessage = "User with such id does not exist";
        //WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(messageService.getMessage(errorCode)).thenReturn(expectedErrorMessage);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl.patchUser(userId, userPatchDto));
        //THEN
        verify(userMapper, never()).patch(eq(userPatchDto), any());
        verify(userRepository, never()).save(any());
        verify(userRepository, times(1)).findById(userId);
        verify(messageService, times(1)).getMessage(errorCode);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void shouldDeleteUserFromUserId() {
        //GIVEN
        Long userId = 1L;
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        //WHEN
        when(userRepository.existsById(userId)).thenReturn(true);
        userServiceImpl.deleteUser(userId);
        //THEN
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository).deleteById(userIdArgumentCaptor.capture());
        assertEquals(userId, userIdArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserFromUserId() {
        //GIVEN
        Long userId = 1L;
        String errorCode = "user.with.such.id.does.not.exist";
        String expectedErrorMessage = "User with such id does not exist";
        //WHEN
        when(userRepository.existsById(userId)).thenReturn(false);
        when(messageService.getMessage(errorCode)).thenReturn(expectedErrorMessage);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUser(userId));
        //THEN
        verify(userRepository, never()).deleteById(userId);
        verify(userRepository, times(1)).existsById(userId);
        verify(messageService, times(1)).getMessage(errorCode);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

}
