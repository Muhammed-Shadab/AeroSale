package com.miniProject.AeroScale.order.exception;

public class ProductIsNotAvailable extends RuntimeException{

    public ProductIsNotAvailable(String msg) {
        super(msg);
    }
}
