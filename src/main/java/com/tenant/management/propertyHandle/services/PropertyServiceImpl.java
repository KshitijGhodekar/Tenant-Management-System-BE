package com.tenant.management.propertyHandle.services;

import com.tenant.management.propertyHandle.entities.Property;
import com.tenant.management.propertyHandle.factories.PropertyFactory;
import com.tenant.management.propertyHandle.repositories.PropertyRepository;
import com.tenant.management.propertyHandle.repositories.PropertyService;
import com.tenant.management.propertyHandle.requestdtos.AddPropertyRequest;
import com.tenant.management.propertyHandle.requestdtos.PropertyResponse;
import com.tenant.management.propertyHandle.requestdtos.UpdatePropertyRequest;

import com.tenant.management.utils.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RestController
@RequestMapping("/api/properties")
public class PropertyServiceImpl implements PropertyService {

    private static final String PROPERTY_NOT_FOUND_MESSAGE = "Property not found";
    private static final String PROPERTY_NOT_FOUND_ID_MESSAGE = "Property not found with ID:";
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }


    @Override
    public ApiResponse addProperty(AddPropertyRequest addPropertyRequest) {
        // Validate the price and address
        if (addPropertyRequest.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (addPropertyRequest.getAddress() == null || addPropertyRequest.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        if (addPropertyRequest.getLandlordId() == null || addPropertyRequest.getLandlordId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Landlord ID cannot be null or empty.");
        }

        Property property = PropertyFactory.createProperty( // Implemented Factory Design Pattern
                addPropertyRequest.getPropertyTitle(),
                addPropertyRequest.getType(),
                addPropertyRequest.getAddress(),
                addPropertyRequest.getPrice(),
                addPropertyRequest.getBedrooms(),
                addPropertyRequest.getBathrooms(),
                addPropertyRequest.getAvailable(),
                addPropertyRequest.getLandlordId()
        );
        // Save property to the repository
        propertyRepository.save(property);

        return ApiResponse.builder()
                .success(true)
                .message("Property added successfully")
                .build();
    }

    @Override
    public ApiResponse updateProperty(UUID propertyId, UpdatePropertyRequest updateRequest) {
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if (optionalProperty.isEmpty()) {
            throw new IllegalArgumentException(PROPERTY_NOT_FOUND_ID_MESSAGE + propertyId);
        }
        Property property = optionalProperty.get();
        if (updateRequest.getAddress() != null) property.setAddress(updateRequest.getAddress());
        if (updateRequest.getPrice() != null) property.setPrice(updateRequest.getPrice());
        if (updateRequest.getType() != null) property.setType(updateRequest.getType());
        if (updateRequest.getBedrooms() != null) property.setBedrooms(updateRequest.getBedrooms());
        if (updateRequest.getBathrooms() != null) property.setBathrooms(updateRequest.getBathrooms());
        if (updateRequest.getAvailable() != null) property.setAvailable(updateRequest.getAvailable());
        propertyRepository.save(property);
        return ApiResponse.builder().success(true).message("Property updated successfully").build();
    }


    @Override
    public PropertyResponse getPropertyById(UUID propertyId) {
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            return mapToResponse(property);
        } else {
            throw new IllegalArgumentException(PROPERTY_NOT_FOUND_ID_MESSAGE + propertyId);
        }
    }

    @Override
    public ApiResponse deleteProperty(UUID propertyId) {
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if (optionalProperty.isEmpty()) {
            return ApiResponse.builder().success(false).message(PROPERTY_NOT_FOUND_MESSAGE).build();
        }
        Property property = optionalProperty.get();
        if (Boolean.FALSE.equals(property.getAvailable())) {
            throw new IllegalStateException("Cannot delete an already deleted property");
        }
        propertyRepository.delete(property);
        return ApiResponse.builder().success(true).message("Property deleted successfully").build();
    }


    @Override
    public List<PropertyResponse> searchProperties(String location, String propertyTitle,Double minPrice, Double maxPrice, String type, Integer bedrooms, Integer bathrooms, Boolean available) {
        List<Property> properties = propertyRepository.searchProperties(location, propertyTitle, minPrice, maxPrice, type, bedrooms, bathrooms, available);
        return properties.stream().map(this::mapToResponse).toList();
    }

    // Helper method to map Property entity to PropertyResponse DTO
    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setPropertyId(property.getId());
        response.setAddress(property.getAddress());
        response.setPropertyTitle(property.getPropertyTitle());
        response.setPrice(property.getPrice());
        response.setType(property.getType());
        response.setBedrooms(property.getBedrooms());
        response.setBathrooms(property.getBathrooms());
        response.setAvailable(property.getAvailable());
        response.setLandlordId(property.getLandlordId());
        return response;
    }

    @Override
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }
}