package com.bnpp.katas.developmentbooks.exception;

public class InvalidBookException extends RuntimeException {
    public InvalidBookException (String message) {
        super(message);
    }
}
