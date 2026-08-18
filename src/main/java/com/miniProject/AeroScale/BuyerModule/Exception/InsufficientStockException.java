package com.miniProject.AeroScale.BuyerModule.Exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String msg) {
        super(msg);

    }
}
