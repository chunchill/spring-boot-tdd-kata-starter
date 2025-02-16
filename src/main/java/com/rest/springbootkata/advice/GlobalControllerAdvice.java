package com.rest.springbootkata.advice;

import com.rest.springbootkata.exception.NoCompanyFoundException;
import com.rest.springbootkata.exception.NoEmployeeFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoEmployeeFoundException.class, NoCompanyFoundException.class})
    public ErrorResponse handleNotFoundException(Exception exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
