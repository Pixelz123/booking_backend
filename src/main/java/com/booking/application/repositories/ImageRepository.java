package com.booking.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.application.entites.ImageEntity;

public interface  ImageRepository extends JpaRepository<ImageEntity, String> {
        @Query("""
            SELECT pi.image_src
            FROM ImageEntity pi JOIN pi.property
            WHERE pi.property.propertyId = :property_id
            """)                                    
    public List<String> getImage(@Param("property_id") String property_id);
}
