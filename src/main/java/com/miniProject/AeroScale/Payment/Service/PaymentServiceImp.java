package com.miniProject.AeroScale.Payment.Service;

import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;
import com.miniProject.AeroScale.Payment.Entity.Transactions;
import com.miniProject.AeroScale.Payment.Exception.RazorPayOrderIdNotFound;
import com.miniProject.AeroScale.Payment.TransactionsRepository;
import com.miniProject.AeroScale.order.entity.Orders;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PaymentServiceImp implements PaymentService{

    @Value("${razorpay-key}")
    private String key;

    @Value("${razorpay-secret-key}")
    private String secretKey;
    private TransactionsRepository transactionsRepository;

    @Override
    public RazorPayOrderResponse createRazorPayOrder(Orders savedOrder) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secretKey);

        int amount = savedOrder.getTotalAmount()
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .intValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "AeroScale_" + savedOrder.getId());
        JSONObject notes = new JSONObject();
        notes.put("note1","AeroScale");
        orderRequest.put("notes",notes);

        Order order = razorpay.orders.create(orderRequest);

        Transactions transactions = Transactions.builder()
                .orderId(UUID.randomUUID())
                .RazorPayoOrderId(order.get("id"))
                .amount(savedOrder.getTotalAmount())
                .status(Transactions.Status.PENDING)
                .build();

        transactionsRepository.save(transactions);

        RazorPayOrderResponse response = new RazorPayOrderResponse(key, amount,order.get("id"));
        return response;
    }

    @Override
    public String getRazorPayOrderId(UUID id) {
        return transactionsRepository.findByOrderId(id).
                orElseThrow(() -> new RazorPayOrderIdNotFound("RazorPay Order Id not found"));
    }

}
