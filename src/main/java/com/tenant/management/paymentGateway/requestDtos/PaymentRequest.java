package com.tenant.management.paymentGateway.requestDtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {
    private Long UserId;
    private UUID propertyId;
    private Double amount;
    private String paymentMethod;
}
