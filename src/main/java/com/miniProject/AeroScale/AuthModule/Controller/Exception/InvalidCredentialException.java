package com.miniProject.AeroScale.AuthModule.Controller.Exception;

public class InvalidCredentialException extends RuntimeException{

    public InvalidCredentialException(String message) {
        super(message);
    }
}
