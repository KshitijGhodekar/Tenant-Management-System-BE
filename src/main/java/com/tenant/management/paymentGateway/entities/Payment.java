package com.tenant.management.paymentGateway.entities;

import com.tenant.management.propertyHandle.entities.Property;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property propertyId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date paymentDate;

    @Column(nullable = false)
    private String paymentMethod; // "CARD", "UPI" , "Apple Pay"

    private String transactionId; // Transaction Id

    private String gatewayResponse; // Payment Response
}
