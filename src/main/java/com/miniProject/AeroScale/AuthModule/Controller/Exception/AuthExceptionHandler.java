package com.miniProject.AeroScale.AuthModule.Controller.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException userAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(userAlreadyExistsException.getMessage());
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<?> invalidCredentialExceptionHandler(InvalidCredentialException invalidCredentialException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(invalidCredentialException.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<?> accountLockedExceptionHandler(AccountLockedException accountLockedException) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(accountLockedException.getMessage());
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<?> refreshTokenExceptionHandler(RefreshTokenException refreshTokenException) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(refreshTokenException.getMessage());
    }


}
