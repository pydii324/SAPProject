package com.demo.exception.cart;

public class CartNotExistsException extends Exception{
    public CartNotExistsException(String msg) {
        super(msg);
    }
}
