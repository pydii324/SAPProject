package com.demo.exception.user;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
