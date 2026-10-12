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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class PaymentServiceImp implements PaymentService {

    @Value("${razorpay-key}")
    private String key;

    @Value("${razorpay-secret-key}")
    private String secretKey;

    private final TransactionsRepository transactionsRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public PaymentServiceImp(TransactionsRepository transactionsRepository,
                             OrderRepository orderRepository,
                             @Lazy OrderService orderService) {
        this.transactionsRepository = transactionsRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public RazorPayOrderResponse createRazorPayOrder(Orders savedOrder) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secretKey);

        // rupees -> paise
        int amount = savedOrder.getTotalAmount()
                .setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .intValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "AeroScale_" + savedOrder.getId());
        JSONObject notes = new JSONObject();
        notes.put("note1", "AeroScale");
        orderRequest.put("notes", notes);

        Order order = razorpay.orders.create(orderRequest);
        String rzpOrderId = order.get("id");

        Transactions transactions = Transactions.builder()
                .orderId(savedOrder.getId())
                .razorpayOrderId(rzpOrderId)
                .amount(savedOrder.getTotalAmount())
                .status(Transactions.Status.PENDING)
                .build();
        transactionsRepository.save(transactions);

        return new RazorPayOrderResponse(key, amount, rzpOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRazorPayOrderId(UUID orderId) {
        return transactionsRepository.findByOrderId(orderId)
                .map(Transactions::getRazorpayOrderId)
                .orElseThrow(() -> new RazorPayOrderIdNotFound("RazorPay Order Id not found"));
    }

    @Override
    @Transactional
    public void updatePaymentStatus(PaymentStatus payload, UUID buyerId) throws Exception {
        Transactions tx = transactionsRepository.findByRazorpayOrderId(payload.getRzpOrderId())
                .orElseThrow(() -> new RazorPayOrderIdNotFound("RazorPay Order Id not found"));

        // order isi buyer ka hona chahiye
        orderRepository.findByIdAndBuyerId(tx.getOrderId(), buyerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // double submit: pehle hi COMPLETED hai to dobara kuch nahi karna
        if (tx.getStatus() == Transactions.Status.COMPLETED) return;

        if (payload.getPaymentId() == null || payload.getSignature() == null
                || !verifySignature(payload.getRzpOrderId(), payload.getPaymentId(), payload.getSignature())) {
            // exception se transaction rollback hota hai, aur wahi chahiye: galat signature se
            // asli transaction ko FAILED mark nahi karna, buyer sahi payment se dobara try kar sake
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment verification failed");
        }

        tx.setStatus(Transactions.Status.COMPLETED);
        tx.setPaymentId(payload.getPaymentId());
        tx.setSignature(payload.getSignature());

        // order CONFIRMED + stock confirm. Ye fail hua to upar ka COMPLETED bhi rollback ho jata hai
        orderService.confirmOrderPayment(tx.getOrderId());
    }

    private boolean verifySignature(String razorpayOrderId, String razorpayPaymentId,
                                    String razorpaySignature) throws Exception {
        String data = razorpayOrderId + "|" + razorpayPaymentId;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Razorpay signature hex hoti hai (Base64 nahi)
        String generated = HexFormat.of().formatHex(hash);

        return MessageDigest.isEqual(
                generated.getBytes(StandardCharsets.UTF_8),
                razorpaySignature.getBytes(StandardCharsets.UTF_8));
    }
}