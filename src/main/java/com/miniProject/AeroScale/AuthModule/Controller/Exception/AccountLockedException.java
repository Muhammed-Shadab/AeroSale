package com.miniProject.AeroScale.AuthModule.Controller.Exception;

public class AccountLockedException extends RuntimeException{

    public AccountLockedException(String msg) {
        super(msg);
    }
}
