package com.rest.springbootkata.exception;

public class NoEmployeeFoundException extends RuntimeException {
    public NoEmployeeFoundException() {
        super("No employee found");
    }
}
