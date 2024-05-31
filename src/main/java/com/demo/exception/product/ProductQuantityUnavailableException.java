package com.demo.exception.product;

public class ProductQuantityUnavailableException extends Exception{
    public ProductQuantityUnavailableException(String msg){
        super(msg);
    }
}
