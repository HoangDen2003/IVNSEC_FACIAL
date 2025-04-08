package com.facial.mapper;

import org.mapstruct.Mapper;

import com.facial.dto.request.AuthenticationRequest;
import com.facial.dto.response.AuthenticationResponse;
import com.facial.entity.User;

@Mapper()
public interface UserMapper {
    User toUser(AuthenticationRequest authenticationRequest);

    AuthenticationResponse toAuthenticationReponse(User user, String token);
}
