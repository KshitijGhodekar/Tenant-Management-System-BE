package com.tenant.management.propertyHandle.factories;

import com.tenant.management.propertyHandle.entities.Apartment;
import com.tenant.management.propertyHandle.entities.Condo;
import com.tenant.management.propertyHandle.entities.House;
import com.tenant.management.propertyHandle.entities.Property;

import java.util.UUID;

public class PropertyFactory {

    private PropertyFactory() {
        throw new UnsupportedOperationException("This class has no instance");
    }

    public static Property createProperty(String propertyTitle, String type, String address, double price,
                                          int bedrooms, int bathrooms, boolean available, UUID landlordId) {
        // Check the type of the property and create an instance accordingly
        switch (type.toLowerCase()) {
            case "apartment":
                return new Apartment(propertyTitle, address, price, bedrooms, bathrooms, available, landlordId);
            case "house":
                return new House(propertyTitle, address, price, bedrooms, bathrooms, available, landlordId);
            case "condo":
                return new Condo(propertyTitle, address, price, bedrooms, bathrooms, available, landlordId);
            default:
                throw new IllegalArgumentException("Unknown property type: " + type);
        }
    }
}