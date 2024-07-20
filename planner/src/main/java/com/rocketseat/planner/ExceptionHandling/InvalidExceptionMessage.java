package com.rocketseat.planner.ExceptionHandling;

public class InvalidExceptionMessage extends RuntimeException {
    public InvalidExceptionMessage(String message) {
        super(message);
    }
}
