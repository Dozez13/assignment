package com.example.assignment.core.mapper;

import com.example.assignment.core.dto.UserGetDto;
import com.example.assignment.core.dto.UserPatchDto;
import com.example.assignment.core.dto.UserPostPutDto;
import com.example.assignment.domain.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserGetDto toUserGetDto(User user);

    User fromUserPostPutDto(UserPostPutDto userPostPutDto);

    void update(UserPostPutDto userPostPutDto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UserPatchDto userPostPutDto, @MappingTarget User user);


    @Condition
    default <T> boolean isPresent(JsonNullable<T> wrapper) {
        return wrapper.isPresent();
    }

    default <T> JsonNullable<T> wrap(T value) {
        return JsonNullable.of(value);
    }

    default <T> T unwrap(JsonNullable<T> wrapper) {
        return wrapper == null ? null : wrapper.orElse(null);
    }
}
