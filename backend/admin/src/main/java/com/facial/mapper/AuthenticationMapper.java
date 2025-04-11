package com.facial.mapper;

import java.util.Date;

import org.mapstruct.Mapper;

import com.facial.dto.response.AuthenticationResponse;
import com.facial.entity.User;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    //    User toUser(AuthenticationRequest authenticationRequest);

    AuthenticationResponse toAuthenticationReponse(User user, String token, Date expiresAt);
}
