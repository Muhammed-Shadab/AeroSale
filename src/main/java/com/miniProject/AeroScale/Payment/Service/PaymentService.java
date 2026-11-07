package com.miniProject.AeroScale.Payment.Service;

import com.miniProject.AeroScale.Payment.DTO.Request.PaymentStatus;
import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public interface PaymentService{
    RazorPayOrderResponse createRazorPayOrder(UUID orderId, BigDecimal amount) throws RazorpayException;

    String getRazorPayOrderId(UUID id);

    void updatePaymentStatus(PaymentStatus paymentUpdate, UUID id) throws Exception;

    ResponseEntity<RazorPayOrderResponse> getRazorPayOrderIdByOrderId(UUID orderId);
}
