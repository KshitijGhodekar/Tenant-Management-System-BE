package com.tenant.management.propertyHandle.repositories;

import com.tenant.management.propertyHandle.requestdtos.AddPropertyRequest;
import com.tenant.management.propertyHandle.requestdtos.PropertyResponse;
import com.tenant.management.propertyHandle.requestdtos.UpdatePropertyRequest;
import com.tenant.management.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface PropertyService {

    ApiResponse addProperty(AddPropertyRequest addPropertyRequest);

    ApiResponse updateProperty(UUID propertyId, UpdatePropertyRequest updateRequest);

    PropertyResponse getPropertyById(UUID propertyId);

    ApiResponse deleteProperty(UUID propertyId);

    List<PropertyResponse> getAllProperties();

    List<PropertyResponse> searchProperties(String location,String propertyTitle ,Double minPrice, Double maxPrice, String type, Integer bedrooms, Integer bathrooms, Boolean available);
}