package com.tenant.management.propertyHandle.requestdtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PropertyResponse {
    private UUID propertyId;
    private String address;
    private String propertyTitle;
    private Double price;
    private String type;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean available;
    private UUID landlordId;
}