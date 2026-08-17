package com.miniProject.AeroScale.AuthModule.Exception;

public class AccountLockedException extends RuntimeException{

    public AccountLockedException(String msg) {
        super(msg);
    }
}
