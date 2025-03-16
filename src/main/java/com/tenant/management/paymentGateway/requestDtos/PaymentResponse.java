package com.tenant.management.paymentGateway.requestDtos;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentResponse {
    private String transactionId;
    private String status;
    private Double amount;
    private Date paymentDate;
}
