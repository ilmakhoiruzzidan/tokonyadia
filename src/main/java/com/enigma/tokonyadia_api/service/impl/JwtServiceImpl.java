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
import com.enigma.tokonyadia_api.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${tokonyadia.jwt.sercret-key}")
    private String SECRET_KEY;
    @Value("${tokonyadia.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;
    @Value("${tokonyadia.jwt.issuer}")
    private String ISSUER;

    private final RedisService redisService;
    private final String BLACKLISTED = "BLACKLISTED";


    @Override
    public String generateAccessToken(UserAccount userAccount) {
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
    public String getUserId(String token) {
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.ERROR_INVALID_JWT_TOKEN);
        }
    }

    private String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return parseToken(bearerToken);
    }

    private DecodedJWT extractClaimJWT(String token) {
        log.info("Extract Token JWT - {}", System.currentTimeMillis());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            log.error("Error while validate JWT Token: {}", exception.getMessage());
            return null;
        }
    }

    @Override
    public void blacklistAccessToken(String bearerToken) {
        String token = parseToken(bearerToken);

        DecodedJWT decodedJWT = extractClaimJWT(token);

        if (decodedJWT == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.ERROR_INVALID_JWT_TOKEN);
        }

        Date expiresAt = decodedJWT.getExpiresAt();
        long timeLeft = (expiresAt.getTime() - System.currentTimeMillis());

        redisService.save(token, BLACKLISTED, Duration.ofMillis(timeLeft));
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return false;
    }
}
