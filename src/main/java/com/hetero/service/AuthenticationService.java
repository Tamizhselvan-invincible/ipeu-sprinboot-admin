package com.hetero.service;

import com.hetero.models.Role;
import com.hetero.models.Token;
import com.hetero.repository.TokenDao;
import com.hetero.repository.UserDao;
import com.hetero.models.User;
import com.hetero.models.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthenticationService {

    private final UserDao userDao;


    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final TokenDao tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserDao repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenDao tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.userDao = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {

        if (request.getmPin() == null || request.getmPin().isEmpty()) {
            throw new IllegalArgumentException("M-PIN cannot be null or empty");
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Mobile Number cannot be null or empty");
        }


        // check if user already exist. if exist than authenticate the user
        if(userDao.findByMobileNo(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, null,"User already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getUserRole());

         if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getMobileNo() != null) user.setMobileNo(request.getMobileNo());
        if (request.getmPin() != null) user.setmPin(request.getmPin());
        if (request.getProfilePicture() != null) user.setProfilePicture(request.getProfilePicture());
        if (request.getAccountStatus() != null) user.setAccountStatus(request.getAccountStatus());
        if (request.getAppVersion() != null) user.setAppVersion(request.getAppVersion());
        if (request.getLastLoginTime() != null) user.setLastLoginTime(request.getLastLoginTime());
        if (request.getUserRole() != null){
            user.setUserRole(request.getUserRole());
        }
        else if (request.getUserRole() == null) {
           user.setUserRole(Role.USER);
        }

        if (request.getAppUpdatedAt() != null) user.setAppUpdatedAt(request.getAppUpdatedAt());
        if (request.getDeviceBrandName() != null) user.setDeviceBrandName(request.getDeviceBrandName());
        if (request.getDeviceVersionCode() != null) user.setDeviceVersionCode(request.getDeviceVersionCode());
        if (request.getOsType() != null) user.setOsType(request.getOsType());
        if(request.getTokens() != null)
            user.setTokens(request.getTokens());



        user = userDao.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");

    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userDao.findByMobileNo(request.getUsername()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract username from token
        String username = jwtService.extractUsername(token);

        // check if the user exist in database
        User user = userDao.findByMobileNo(username)
                .orElseThrow(()->new RuntimeException("No user found"));

        // check if the token is valid
        if(jwtService.isValidRefreshToken(token, user)) {
            // generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, refreshToken, "New token generated"), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
