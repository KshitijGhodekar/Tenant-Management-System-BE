package com.tenant.management.paymentGateway.controller;

import com.tenant.management.paymentGateway.requestDtos.PaymentRequest;
import com.tenant.management.paymentGateway.requestDtos.PaymentResponse;
import com.tenant.management.paymentGateway.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{paymentId}")
    public ResponseEntity<PaymentResponse> verifyPayment(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.verifyPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/success/{paymentId}")
    public ResponseEntity<PaymentResponse> markPaymentAsSuccess(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.markPaymentAsSuccess(paymentId);
        return ResponseEntity.ok(response);
    }


}
