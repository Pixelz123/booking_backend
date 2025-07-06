package com.booking.application.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.application.entites.PropertyEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    @Query(value="""
          SELECT p.property_id,
                 u.username,
                 p.city,
                 p.hero_image_src,
                 p.price_per_night
          FROM properties p LEFT JOIN users u ON p.user_id=u.user_id
          WHERE
               p.address LIKE %:address% AND  
               p.price_per_night BETWEEN  :minPrice AND :maxPrice AND 
               p.property_id NOT IN (
                    SELECT property_id 
                    FROM bookings
                    WHERE cheak_in<= :cheakIn AND cheak_out>= :cheakOut
               );
    """,nativeQuery=true)
    public List<Object[]> getProperties(@Param("address") String locationQueryString,
                                        @Param("cheakIn") Date cheakIn,
                                        @Param("cheakOut") Date cheakOut,
                                        @Param("minPrice") double minPrice,
                                        @Param("maxPrice") double maxPrice);
    
    @Query(value="""
            SELECT p.property_id,
                   u.username,
                   u.user_id,
                   p.description,
                   p.city,
                   p.state,
                   p.country,
                   p.postal_code,
                   p.address,
                   p.price_per_night
            FROM properties p LEFT JOIN users u
                                     ON u.user_id=p.user_id
             WHERE p.property_id= :property_id; 
               
            """,nativeQuery=true)
    public List<Object[]> getPropertyDetails(@Param("property_id") String property_id);
    
                                    
}
