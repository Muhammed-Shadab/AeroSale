package com.miniProject.AeroScale.product.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.miniProject.AeroScale.product")
public class ProductExceptionHandler {

    // 1. Handle Product Not Found
    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Product Not Found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    // 2. Handle Validation Errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields");
        problemDetail.setTitle("Bad Request");

        // Extract specific field errors from the exception
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("errors", fieldErrors);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    // 3. Handle Malformed JSON Data like oif user puts price = abcd like this
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request or invalid data types provided");
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    // 4. Handle Concurrent Modification (Flash Sale Protection)...2 ppl trying to modify same stuff...so this is important for our @version which we hv added
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The product was updated by another transaction. Please refresh and try again.");
        problemDetail.setTitle("Conflict - Concurrent Update");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;



    }
}