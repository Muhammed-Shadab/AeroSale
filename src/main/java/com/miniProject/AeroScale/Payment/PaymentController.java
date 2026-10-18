package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.DTO.Request.PaymentStatus;
import com.miniProject.AeroScale.Payment.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<Void> setPaymentUpdate(@Valid @RequestBody PaymentStatus paymentUpdate,
                                                 @AuthenticationPrincipal(expression = "id") UUID id) throws Exception {
        paymentService.updatePaymentStatus(paymentUpdate, id);
        return ResponseEntity.ok().build();
    }
}
// order_TpJJMh7wW00tEa
//        pay_TpJJTK8u0WK14b
//        4cfda6dd15accd31429fd47d3fb73a75b1f905819f5c00c02ef2a9c1f7e79d87