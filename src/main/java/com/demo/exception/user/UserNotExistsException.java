package com.demo.exception.user;

public class UserNotExistsException extends Exception{
    public UserNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
