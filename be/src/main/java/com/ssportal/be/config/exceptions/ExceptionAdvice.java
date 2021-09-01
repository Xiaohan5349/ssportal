package com.ssportal.be.config.exceptions;

import com.ssportal.be.utilility.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleNotFoundException(RuntimeException ex) {
        ApiResponse apiResponse = new ApiResponse(400, "Bad request", ex.getMessage());
        return apiResponse;
    }

}
