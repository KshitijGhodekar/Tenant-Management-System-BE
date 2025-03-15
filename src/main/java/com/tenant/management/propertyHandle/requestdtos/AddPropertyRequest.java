package com.tenant.management.propertyHandle.requestdtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddPropertyRequest {
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Property Title is required")
    private String propertyTitle;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Bedrooms is required")
    @Min(value = 0, message = "Number of bedrooms cannot be negative")
    private Integer bedrooms;

    @NotNull(message = "Bathrooms is required")
    @Min(value = 0, message = "Number of bathrooms cannot be negative")
    private Integer bathrooms;

    @NotNull(message = "Availability status is required")
    private Boolean available;

    @NotNull(message = "Landlord ID is required")
    private UUID landlordId;
}