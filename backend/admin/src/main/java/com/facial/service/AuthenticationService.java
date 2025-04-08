package com.facial.service;

import org.springframework.stereotype.Service;

import com.facial.configuration.JwtProvider;
import com.facial.dto.request.AuthenticationRequest;
import com.facial.dto.response.AuthenticationResponse;
import com.facial.entity.User;
import com.facial.mapper.UserMapper;
import com.facial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    JwtProvider jwtProvider;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByEmail(authenticationRequest.getEmail());

        //        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        //        boolean authenticated = passwordEncoder.matches(userRequest.getPassword(), user.getPassword());
        //        if (!authenticated) throw new AppException(ErrorCode.USER_NOT_EXISTED);

        // generate token
        var token = jwtProvider.generateToken(user);
        //
        //        InvalidatedToken invalidatedToken = new InvalidatedToken();
        //        invalidatedToken.setId(token);

        return userMapper.toAuthenticationReponse(user, token);
    }
}
