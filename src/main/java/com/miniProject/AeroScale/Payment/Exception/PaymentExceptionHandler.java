package com.miniProject.AeroScale.Payment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice(basePackages = "com.miniProject.AeroScale.Payment")
public class PaymentExceptionHandler {

    @ExceptionHandler(RazorPayOrderIdNotFound.class)
    public ProblemDetail handleNotFound(RazorPayOrderIdNotFound ex) {
        return problem(HttpStatus.NOT_FOUND, "Payment Not Found", ex.getMessage());
    }

    // order confirm nahi ho paya (jaise hold expire ho chuka)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        return problem(HttpStatus.CONFLICT, "Order Cannot Be Confirmed", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid payment payload");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setTitle(title);
        p.setProperty("timestamp", Instant.now());
        return p;
    }
}