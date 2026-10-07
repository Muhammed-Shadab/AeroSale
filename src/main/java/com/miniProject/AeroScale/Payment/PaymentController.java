package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

//    @PostMapping("/CreateRazorPayOrder")
//    public orderResponse temp() throws RazorpayException {
//        return paymentService.createRazorPayOrder();
//    }
//

}
