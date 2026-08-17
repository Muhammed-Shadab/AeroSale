package com.miniProject.AeroScale.AuthModule.Exception;

public class InvalidCredentialException extends RuntimeException{

    public InvalidCredentialException(String message) {
        super(message);
    }
}
