package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.DTO.Request.PaymentStatus;
import com.miniProject.AeroScale.Payment.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private void setPaymentUpdate(@Valid @RequestBody PaymentStatus paymentUpdate,
                                  @AuthenticationPrincipal(expression = "id") UUID id) throws Exception {
        paymentService.updatePaymentStatus(paymentUpdate, id);
    }

}
