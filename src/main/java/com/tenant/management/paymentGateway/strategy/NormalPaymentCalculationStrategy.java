package com.tenant.management.paymentGateway.strategy;

import com.tenant.management.propertyHandle.entities.Property;

public class NormalPaymentCalculationStrategy implements PaymentCalculationStrategy {

    @Override
    public double calculateFinalAmount(Property property) {
        double basePrice = property.getPrice();
        double discount = 0.15;
        double discountedPrice = basePrice - (basePrice * discount);
        return applyVatIfRequired(discountedPrice, property);
    }

    private double applyVatIfRequired(double price, Property property) {
        if (isRentalProperty(property)) {
            double vatRate = 0.20;
            return price + (price * vatRate);
        }
        return price;
    }

    private boolean isRentalProperty(Property property) {
        return property.getType().equalsIgnoreCase("house") ||
                property.getType().equalsIgnoreCase("apartment") ||
                property.getType().equalsIgnoreCase("condo");
    }
}

