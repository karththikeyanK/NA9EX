package com.express.user.exception;

public class DataAccessException extends RuntimeException{
    public DataAccessException(String message) {
        super(message);
    }
}
