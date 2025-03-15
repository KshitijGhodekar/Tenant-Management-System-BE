package com.tenant.management.propertyHandle.controller;

import com.tenant.management.propertyHandle.repositories.PropertyService;
import com.tenant.management.propertyHandle.requestdtos.AddPropertyRequest;
import com.tenant.management.propertyHandle.requestdtos.PropertyResponse;
import com.tenant.management.propertyHandle.requestdtos.UpdatePropertyRequest;
import com.tenant.management.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "http://localhost:3000")
public class PropertyController {

    private final PropertyService propertyService;

    // Constructor injection for PropertyService
    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // Get All Properties
    @GetMapping("/allProperties")
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {
        List<PropertyResponse> properties = propertyService.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    // Add Property
    @PostMapping("/addPropertyDetails")
    public ResponseEntity<ApiResponse> addProperty(@RequestBody AddPropertyRequest addPropertyRequest) {
        ApiResponse response = propertyService.addProperty(addPropertyRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update Property
    @PutMapping("/updatePropertyDetails/{propertyId}")
    public ResponseEntity<ApiResponse> updateProperty(
            @PathVariable UUID propertyId,
            @RequestBody UpdatePropertyRequest updateRequest) {
        ApiResponse response = propertyService.updateProperty(propertyId, updateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get Property by ID
    @GetMapping("getPropertyDetails/{propertyId}")
    public ResponseEntity<PropertyResponse> getProperty(@PathVariable UUID propertyId) {
        PropertyResponse property = propertyService.getPropertyById(propertyId);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    // Delete Property
    @DeleteMapping("/deleteProperty/{propertyId}")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable UUID propertyId) {
        ApiResponse response = propertyService.deleteProperty(propertyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Search Properties
    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponse>> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String propertyTitle,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) Boolean available) {
        List<PropertyResponse> properties = propertyService.searchProperties(location,propertyTitle ,minPrice, maxPrice, type, bedrooms, bathrooms, available);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}