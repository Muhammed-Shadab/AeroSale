package com.miniProject.AeroScale.AuthModule.Controller.Exception;

public class RefreshTokenException extends RuntimeException{

    public RefreshTokenException(String msg) {
        super(msg);
    }
}
