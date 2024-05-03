package com.example.assignment.core.service.impl;

import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.repository.param.UserSearchParameters;
import com.example.assignment.core.exception.UserNotFoundException;
import com.example.assignment.core.mapper.UserMapper;
import com.example.assignment.core.service.MessageService;
import com.example.assignment.core.service.UserService;
import com.example.assignment.domain.entity.User;
import com.example.assignment.domain.repository.CustomizedUserRepository;
import com.example.assignment.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CustomizedUserRepository customizedUserRepository;
    private final MessageService messageService;



    @Override
    public List<UserGetDto> getUserByBirthDateRange(UserSearchParameters userSearchParameters) {
        return customizedUserRepository.retrieveUsersByBirthDateRange(userSearchParameters)
                .stream()
                .map(userMapper::toUserGetDto)
                .collect(Collectors.toList());
    }



    @Transactional
    @Override
    public void createUser(UserPostPutDto userPostPutDto) {
        User user = userMapper.fromUserPostPutDto(userPostPutDto);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(Long id, UserPostPutDto userPostPutDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(messageService.getMessage("user.with.such.id.does.not.exist")));
        userMapper.update(userPostPutDto, user);
        /*
        I'm aware of JPA(Hibernate) dirty checking mechanism but
        the lack of the need to explicitly mark state changes as “done” in JPA is a JPA oddity.
        By no means should service layer code have to know about such an implementation detail.
        */
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void patchUser(Long id, UserPatchDto userPatchDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(messageService.getMessage("user.with.such.id.does.not.exist")));
        userMapper.patch(userPatchDto, user);
          /*
        I'm aware of JPA(Hibernate) dirty checking mechanism but
        the lack of the need to explicitly mark state changes as “done” in JPA is a JPA oddity.
        By no means should service layer code have to know about such an implementation detail.
        */
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(messageService.getMessage("user.with.such.id.does.not.exist"));
        }
        userRepository.deleteById(id);
    }


}
