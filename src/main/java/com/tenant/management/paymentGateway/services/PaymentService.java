package com.tenant.management.paymentGateway.services;

import com.tenant.management.paymentGateway.entities.Payment;
import com.tenant.management.paymentGateway.repositories.PaymentRepository;
import com.tenant.management.paymentGateway.requestDtos.PaymentRequest;
import com.tenant.management.paymentGateway.requestDtos.PaymentResponse;
import com.tenant.management.propertyHandle.entities.Property;
import com.tenant.management.propertyHandle.repositories.PropertyRepository;
import com.tenant.management.userLogin.entities.User;
import com.tenant.management.userLogin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final String paymentGatewayUrl = "https://api.paymentgateway.com";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public PaymentResponse initiatePayment(PaymentRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        Property property = propertyRepository.getById(request.getPropertyId());

        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found"));

        double finalAmount = calculatePaymentAmount(property);

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setPropertyId(property);
        payment.setAmount(finalAmount);
        payment.setStatus("PENDING");
        payment.setPaymentDate(new Date());
        payment.setPaymentMethod(request.getPaymentMethod());

        String transactionId = UUID.randomUUID().toString();
        String gatewayResponse = "Mock response from gateway";

        payment.setTransactionId(transactionId);
        payment.setGatewayResponse(gatewayResponse);
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setTransactionId(transactionId);
        response.setStatus("SUCCESS");
        response.setAmount(finalAmount);
        response.setPaymentDate(payment.getPaymentDate());
        return response;
    }

    private double calculatePaymentAmount(Property property) {
        return property.getPrice();
    }

    public PaymentResponse verifyPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if ("SUCCESS".equals(payment.getStatus())) {
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getStatus());
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }
        if ("PENDING".equals(payment.getStatus())) {
            throw new IllegalStateException("Payment is still pending. Please try again later.");
        }
        if ("FAILED".equals(payment.getStatus())) {
            throw new IllegalStateException("Payment failed. Please check your payment details.");
        }
        throw new IllegalStateException("Payment verification failed: Payment status is not SUCCESS. Current status: " + payment.getStatus());
    }

    public PaymentResponse cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if ("PENDING".equals(payment.getStatus())) {
            payment.setStatus("CANCELLED");
            paymentRepository.save(payment);
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus("CANCELLED");
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }

        throw new IllegalArgumentException("Only PENDING payments can be cancelled");
    }

    public PaymentResponse markPaymentAsSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (!"CANCELLED".equals(payment.getStatus()) && !"SUCCESS".equals(payment.getStatus())) {
            payment.setStatus("SUCCESS");
            payment.setPaymentDate(new Date());
            paymentRepository.save(payment);
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus("SUCCESS");
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }

        throw new IllegalStateException("Payment is already SUCCESS or CANCELLED");
    }
}
