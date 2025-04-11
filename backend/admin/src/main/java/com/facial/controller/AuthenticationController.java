package com.facial.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facial.dto.request.AuthenticationRequest;
import com.facial.dto.response.ApiResponse;
import com.facial.dto.response.AuthenticationResponse;
import com.facial.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.login(authenticationRequest))
                .code(200)
                .message("Login successfully")
                .build();
    }

    //    @PostMapping("/register")
    //    ApiResponse<UserResponse> register(@RequestBody UserRequest userRequest) {
    //        return ApiResponse.<UserResponse>builder()
    //                .result(authenticationService.register(userRequest))
    //                .code(200)
    //                .message("Register successfully")
    //                .build();
    //    }

    @PostMapping("/test")
    ApiResponse<String> test() {
        return ApiResponse.<String>builder()
                .result(authenticationService.check())
                .code(200)
                .build();
    }
}
