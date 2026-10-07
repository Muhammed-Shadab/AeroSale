package com.miniProject.AeroScale.Payment.Service;

import com.miniProject.AeroScale.Payment.DTO.Request.PaymentStatus;
import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;
import com.miniProject.AeroScale.order.entity.Orders;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService{
    RazorPayOrderResponse createRazorPayOrder(Orders savedOrder) throws RazorpayException;

    String getRazorPayOrderId(UUID id);

    void updatePaymentStatus(PaymentStatus paymentUpdate, UUID id) throws Exception;
}
