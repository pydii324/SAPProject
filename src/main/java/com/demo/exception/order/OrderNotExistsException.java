package com.demo.exception.order;

public class OrderNotExistsException extends Exception {
    public OrderNotExistsException(String msg) {
        super(msg);
    }
}
