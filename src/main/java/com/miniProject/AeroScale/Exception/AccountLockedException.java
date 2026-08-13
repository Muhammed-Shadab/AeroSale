package com.miniProject.AeroScale.Exception;

public class AccountLockedException extends RuntimeException{

    public AccountLockedException(String msg) {
        super(msg);
    }
}
