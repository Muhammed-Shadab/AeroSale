package com.miniProject.AeroScale.BuyerModule.Exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String msg) {
        super(msg);
    }
}
