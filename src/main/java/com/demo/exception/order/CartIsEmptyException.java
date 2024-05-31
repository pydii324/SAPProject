package com.demo.exception.order;

public class CartIsEmptyException extends Exception{
    public CartIsEmptyException(String msg) {
        super(msg);
    }
}
