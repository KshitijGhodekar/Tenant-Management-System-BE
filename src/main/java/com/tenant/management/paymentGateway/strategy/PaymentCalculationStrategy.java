package com.tenant.management.paymentGateway.strategy;

import com.tenant.management.propertyHandle.entities.Property;


public interface PaymentCalculationStrategy {
    double calculateFinalAmount(Property property);
}
