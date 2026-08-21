package com.miniProject.AeroScale.order.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice(basePackages = "com.miniProject.AeroScale.order.controller")
public class OrderExceptionHandler {

    // 1. Handle our custom Validation Errors
    @ExceptionHandler(OrderValidationException.class)
    public ProblemDetail handleOrderValidation(OrderValidationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Order Validation Failed");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // 2. Handle Empty Cart
    @ExceptionHandler(EmptyCartException.class)
    public ProblemDetail handleEmptyCart(EmptyCartException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Cart is Empty");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // 3. Handle Product Constraints (Thrown from ProductService)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Item Unavailable");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // 4. THE FLASH SALE LOCK (Concurrency Collision)
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLocking(OptimisticLockingFailureException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "High traffic alert: The product stock changed while processing your order. Please retry checkout.");
        problem.setTitle("Checkout Conflict");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}