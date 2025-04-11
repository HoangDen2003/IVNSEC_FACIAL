package com.facial.mapper;

import org.mapstruct.Mapper;

import com.facial.dto.request.UserRequest;
import com.facial.dto.response.UserResponse;
import com.facial.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);

    UserResponse toUserRepspone(User user);
}
