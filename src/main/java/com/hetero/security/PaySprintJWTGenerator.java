package com.hetero.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Base64;

@Configuration
public class PaySprintJWTGenerator {

//    public static void main(String[] args){
//        PaySprintJWTGenerator paySprintJWTGenerator = new PaySprintJWTGenerator();
//        System.out.println("Token is : "+ paySprintJWTGenerator.jwtSecretKey);
//    }

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${application.pay-sprint.partner-id}")
    private String partnerId;



    private int getRandom() {
        int random = (int) Math.round(Math.random() * 1000000000);
        return random;
    }

    private  long getTimestamp() {
        long timestamp = Instant.now().getEpochSecond();
        return timestamp;
    }

    public  String getToken() {

        int random = getRandom();
        long timestamp = getTimestamp();
        String secretKey =  Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        String token = io.jsonwebtoken.Jwts.builder()
                .setIssuer("PSPRINT")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("iss", "PSPRINT") // not Compulsary
                .claim("timestamp", timestamp)
                .claim("partnerId", partnerId) // PARTNER ID
                .claim("product","BUSTICKET") // not Compulsary
                .claim("reqid", random) // Random request no
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }
}

