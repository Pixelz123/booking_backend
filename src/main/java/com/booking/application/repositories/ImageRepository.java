package com.booking.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.application.entites.ImageEntity;

public interface  ImageRepository extends JpaRepository<ImageEntity, String> {
        @Query(value="""
            SELECT image_src
            FROM property_images
            WHERE property_id = :property_id
            """,nativeQuery=true)                                    
    public List<Object[]> getImage(@Param("property_id") String property_id);
}
