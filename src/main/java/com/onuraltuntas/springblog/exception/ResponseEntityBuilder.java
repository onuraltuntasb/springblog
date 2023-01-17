package com.onuraltuntas.springblog.exception;

import com.onuraltuntas.springblog.model.ApiErrorResponse;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    public static ResponseEntity<Object> build(ApiErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}