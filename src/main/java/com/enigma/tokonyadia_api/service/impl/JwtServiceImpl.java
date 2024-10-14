package com.enigma.tokonyadia_api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${tokonyadia.jwt.sercret-key}")
    private String SECRET_KEY;
    @Value("${tokonyadia.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;
    @Value("${tokonyadia.jwt.issuer}")
    private String ISSUER;

    @Override
    public String generateToken(UserAccount userAccount) {
        log.info("Generating JWT Token for User: {}", userAccount.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(ISSUER) // ini di payload
                    .withIssuedAt(Instant.now()) // ini di payload
                    .withExpiresAt(Instant.now().plus(EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES)) // ini di payload
                    .withSubject(userAccount.getId()) // ini di payload
                    .withClaim("role", userAccount.getRole().getDescription()) // ini di payload
                    .sign(algorithm);// ini di payload
        } catch (JWTCreationException e) {
            log.error("Error creating JWT Token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.ERROR_CREATE_JWT);
        }
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public String getUserIdFromToken(String token) {
        log.info("Extract JWT Token - {}", System.currentTimeMillis());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer(ISSUER)
                    // reusable verifier instance
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            log.error("Error Extracting JWT with token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.ERROR_CREATE_JWT);
        }
    }
}
