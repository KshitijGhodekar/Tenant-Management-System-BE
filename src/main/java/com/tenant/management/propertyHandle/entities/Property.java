package com.tenant.management.propertyHandle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Property {
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    private String address;
    private String propertyTitle;
    private Double price;
    @Column(nullable = false)
    private String type;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean available;
    @Column(nullable = false)
    private UUID landlordId;
}