package com.enigma.tokonyadia_api.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Error client:{}", response);
        HttpStatus status = HttpStatus.valueOf(response.status());

        return switch (status) {
            case UNAUTHORIZED -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            case BAD_REQUEST -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            case NOT_FOUND -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
            case INTERNAL_SERVER_ERROR -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            default -> new Exception("Generic error");
        };
    }
}
