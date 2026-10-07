package com.miniProject.AeroScale.Payment.Service;

import com.miniProject.AeroScale.Payment.DTO.Request.PaymentStatus;
import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;
import com.miniProject.AeroScale.Payment.Entity.Transactions;
import com.miniProject.AeroScale.Payment.Exception.RazorPayOrderIdNotFound;
import com.miniProject.AeroScale.Payment.TransactionsRepository;
import com.miniProject.AeroScale.order.entity.Orders;
import com.miniProject.AeroScale.order.repository.OrderRepository;
import com.miniProject.AeroScale.order.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PaymentServiceImp implements PaymentService{

    @Value("${razorpay-key}")
    private String key;

    @Value("${razorpay-secret-key}")
    private String secretKey;
    private TransactionsRepository transactionsRepository;
    private OrderService orderService;
    private OrderRepository orderRepository;

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
                .orderId(savedOrder.getId())
                .RazorPayOrderId(order.get("id"))
                .amount(savedOrder.getTotalAmount())
                .status(Transactions.Status.PENDING)
                .build();

        transactionsRepository.save(transactions);

        RazorPayOrderResponse response = new RazorPayOrderResponse(key, amount, order.get("id"));
        return response;
    }

    @Override
    public String getRazorPayOrderId(UUID id) {
        return transactionsRepository.findByOrderId(id).
                orElseThrow(() -> new RazorPayOrderIdNotFound("RazorPay Order Id not found"));
    }

    @Override
    @Transactional
    public void updatePaymentStatus(PaymentStatus payload, UUID id) throws Exception {
        Transactions transactions = transactionsRepository.findTransactionByOrderId(UUID.fromString(payload.getRzpOrderId()))
                .orElseThrow(() -> new RazorPayOrderIdNotFound("RazorPay Order Id not found"));

        Optional<Orders> order = orderRepository.findByIdAndBuyerId(transactions.getOrderId(), id);
        if(order.isEmpty() || payload.getPaymentId() == null || payload.getSignature() == null
                || !(verifySignature(payload.getRzpOrderId(), payload.getPaymentId(), payload.getSignature()))) {
            transactions.setStatus(Transactions.Status.FAILED);
        }else {
            transactions.setStatus(Transactions.Status.COMPLETED);
            transactions.setPaymentId(payload.getPaymentId());
            transactions.setSignature(payload.getSignature());

            orderService.confirmOrderPayment(UUID.fromString(payload.getRzpOrderId()));
        }
    }



    public boolean verifySignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception {

        String payload = razorpayOrderId + "|" + razorpayPaymentId;

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(
                payload.getBytes(StandardCharsets.UTF_8)
        );

        String generatedSignature =
                java.util.Base64.getEncoder().encodeToString(hash);

        return generatedSignature.equals(razorpaySignature);
    }

}
