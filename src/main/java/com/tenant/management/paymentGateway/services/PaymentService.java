package com.tenant.management.paymentGateway.services;

import com.tenant.management.paymentGateway.entities.Payment;
import com.tenant.management.paymentGateway.repositories.PaymentRepository;
import com.tenant.management.paymentGateway.requestDtos.PaymentRequest;
import com.tenant.management.paymentGateway.requestDtos.PaymentResponse;
import com.tenant.management.propertyHandle.entities.Property;
import com.tenant.management.propertyHandle.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // Payment status constants
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    // Error message constants
    private static final String ERROR_PAYMENT_NOT_FOUND = "Payment not found";
    private static final String ERROR_PAYMENT_PENDING = "Payment is still pending. Please try again later.";
    private static final String ERROR_PAYMENT_FAILED = "Payment failed. Please check your payment details.";
    private static final String ERROR_PAYMENT_VERIFICATION_FAILED = "Payment verification failed: Payment status is not SUCCESS. Current status: ";
    private static final String ERROR_PAYMENT_CANCELLATION = "Only PENDING payments can be cancelled";
    private static final String ERROR_PAYMENT_ALREADY_PROCESSED = "Payment is already SUCCESS or CANCELLED";

//    private final String paymentGatewayUrl = "https://api.paymentgateway.com";  // Change API with actual payment

    private final PaymentRepository paymentRepository;
    private final PropertyRepository propertyRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          PropertyRepository propertyRepository) {
        this.paymentRepository = paymentRepository;
        this.propertyRepository = propertyRepository;
    }

    public PaymentResponse initiatePayment(PaymentRequest request) {
        logger.info("Initiating payment for User ID: {} and Property ID: {}", request.getUserId(), request.getPropertyId());
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        double finalAmount = calculatePaymentAmount(property);

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setPropertyId(property);
        payment.setAmount(finalAmount);
        payment.setStatus(STATUS_PENDING);
        payment.setPaymentDate(new Date());
        payment.setPaymentMethod(request.getPaymentMethod());
        String transactionId = UUID.randomUUID().toString();
        String gatewayResponse = "Mock response from gateway";

        logger.info("Generated Transaction ID: {}", transactionId);
        payment.setTransactionId(transactionId);
        payment.setGatewayResponse(gatewayResponse);
        payment.setStatus(STATUS_PENDING);
        paymentRepository.save(payment);

        logger.info("Payment successfully processed for Transaction ID: {}", transactionId);
        PaymentResponse response = new PaymentResponse();
        response.setTransactionId(transactionId);
        response.setStatus(STATUS_PENDING);
        response.setAmount(finalAmount);
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }

    private double calculatePaymentAmount(Property property) {
        return property.getPrice();
    }

    public PaymentResponse verifyPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_PAYMENT_NOT_FOUND));
        logger.info("Payment Status: {}", payment);
        if (STATUS_SUCCESS.equals(payment.getStatus())) {
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getStatus());
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            logger.info("Payment Status: {}", response);
            return response;
        }
        if (STATUS_PENDING.equals(payment.getStatus())) {
            throw new IllegalStateException(ERROR_PAYMENT_PENDING);
        }
        if (STATUS_FAILED.equals(payment.getStatus())) {
            throw new IllegalStateException(ERROR_PAYMENT_FAILED);
        }
        throw new IllegalStateException(ERROR_PAYMENT_VERIFICATION_FAILED + payment.getStatus());
    }

    public PaymentResponse cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_PAYMENT_NOT_FOUND));

        if (STATUS_PENDING.equals(payment.getStatus())) {
            payment.setStatus(STATUS_CANCELLED);
            paymentRepository.save(payment);
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(STATUS_CANCELLED);
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }

        throw new IllegalArgumentException(ERROR_PAYMENT_CANCELLATION);
    }

    public PaymentResponse markPaymentAsSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_PAYMENT_NOT_FOUND));
        if (!STATUS_CANCELLED.equals(payment.getStatus()) && !STATUS_SUCCESS.equals(payment.getStatus())) {
            payment.setStatus(STATUS_SUCCESS);
            payment.setPaymentDate(new Date());
            paymentRepository.save(payment);
            PaymentResponse response = new PaymentResponse();
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(STATUS_SUCCESS);
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }

        throw new IllegalStateException(ERROR_PAYMENT_ALREADY_PROCESSED);
    }
}