package com.facial.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.facial.configuration.JwtProvider;
import com.facial.dto.request.AuthenticationRequest;
import com.facial.dto.response.AuthenticationResponse;
import com.facial.dto.response.JwtTokenResponse;
import com.facial.entity.User;
import com.facial.mapper.AuthenticationMapper;
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
    AuthenticationMapper authenticationMapper;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User user = userRepository
                .findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email was not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) throw new Error("Password is wrong");

        // generate token
        JwtTokenResponse jwtRps = jwtProvider.generateToken(user);

        return authenticationMapper.toAuthenticationReponse(user, jwtRps.getToken(), jwtRps.getExpiresAt());
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //    public UserResponse register(UserRequest userRequest) {
    //        log.info("Vao day roi");
    //
    //        // sai
    ////        User user = userRepository.findByEmail(userRequest.getEmail()).orElse(null);
    ////
    ////        log.info("USER email: " + );
    ////
    ////        if (user != null) {
    ////            throw new Error("Email existed");
    ////        }
    ////
    ////        if (userRequest.getEmail().isEmpty()) {
    ////            throw new Error("Email must be requires");
    ////        }
    //
    //
    //
    //        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    //
    //        User newUser = userMapper.toUser(userRequest);
    //        newUser = userRepository.save(newUser);
    //
    //        return userMapper.toUserRepspone(newUser);
    //    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String check() {
        log.info("ADMIN DA VAO ROI");
        return "ADMIN DA VAO ROI";
    }
}
