package com.hetero.controller;

import com.hetero.models.AuthenticationResponse;
import com.hetero.models.User;
import com.hetero.service.AuthenticationService;
import com.hetero.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/authentication")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody User request
            ) {
        if (request.getmPin() == null || request.getmPin().isEmpty()) {
            throw new IllegalArgumentException("M-PIN cannot be null or empty");
        }
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(202,"User Authenticated",authService.register(request));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "login"; // Returns the login.html page from templates (for Thymeleaf)
//    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(
//            @RequestBody User request
//    ) {
//        return ResponseEntity.ok(authService.authenticate(request));
//    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request
    ) {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(202,"Token Refreshed",authService.refreshToken(request));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }


}