package com.demo.exception.product;

public class ProductAlreadyExistsException extends Exception{
    public ProductAlreadyExistsException(String msg) {
        super(msg);
    }
}
