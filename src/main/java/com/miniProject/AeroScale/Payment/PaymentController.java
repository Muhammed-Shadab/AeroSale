package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.DTO.Response.orderResponse;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.*;

@RestController
@RequestMapping("/api/pay")
public class PaymentController {

    @PostMapping("/test")
    public orderResponse temp() throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient("", "");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",100); // Amount is in currency subunits.
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "receipt#1");
        JSONObject notes = new JSONObject();
        notes.put("notes_key_1","Tea, Earl Grey, Hot");
        orderRequest.put("notes",notes);

        Order order = razorpay.orders.create(orderRequest);
        orderResponse response = new orderResponse("", 100,order.get("id"));
        return response;
    }

//    @GetMapping("/test")
//    public String test() {
//        return "working";
//    }



}
