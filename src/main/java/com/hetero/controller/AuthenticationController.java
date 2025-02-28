package com.hetero.controller;

import com.hetero.models.AuthenticationResponse;
import com.hetero.models.User;
import com.hetero.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
            ) {
        if (request.getmPin() == null || request.getmPin().isEmpty()) {
            throw new IllegalArgumentException("M-PIN cannot be null or empty");
        }
        return ResponseEntity.ok(authService.register(request));
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
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }
}