package com.miniProject.AeroScale.AuthModule.Exception;

import com.miniProject.AeroScale.order.exception.OrderValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(ex.getMessage());
    }


}
