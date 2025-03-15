package com.tenant.management.propertyHandle.repositories;

import com.tenant.management.propertyHandle.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR p.address LIKE %:location%) AND " +
            "(:propertyTitle IS NULL OR p.propertyTitle LIKE %:propertyTitle%) AND " +  // Added propertyTitle filter
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms) AND " +
            "(:bathrooms IS NULL OR p.bathrooms = :bathrooms) AND " +
            "(:available IS NULL OR p.available = :available)")
    List<Property> searchProperties(
            @Param("location") String location,
            @Param("propertyTitle") String propertyTitle,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("type") String type,
            @Param("bedrooms") Integer bedrooms,
            @Param("bathrooms") Integer bathrooms,
            @Param("available") Boolean available);
}