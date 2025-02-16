package com.rest.springbootkata.exception;

public class NoCompanyFoundException extends RuntimeException {
    public NoCompanyFoundException() {
        super("No company found");
    }
}
