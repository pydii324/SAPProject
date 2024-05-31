package com.demo.exception.product;

public class ProductNotExistsException extends Exception {
    public ProductNotExistsException(String msg) {
        super(msg);
    }
}
