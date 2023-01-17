package com.onuraltuntas.springblog.advice;

import com.onuraltuntas.springblog.exception.ResponseEntityBuilder;
import com.onuraltuntas.springblog.exception.TokenRefreshException;
import com.onuraltuntas.springblog.model.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> details = new ArrayList<String>();
        details.add(request.getDescription(false));

        ApiErrorResponse err = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN, ex.getMessage() ,details);

        return ResponseEntityBuilder.build(err);
    }
}