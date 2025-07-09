package com.booking.application.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.application.dto.PropertyDetailDTO;
import com.booking.application.dto.PropertyResponseDTO;
import com.booking.application.entites.PropertyEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
       @Query(value = """
                           SELECT p.property_id,
                                  u.username,
                                  p.city,
                                  p.hero_image_src,
                                  p.price_per_night,
                                  p.name
                           FROM properties p LEFT JOIN users u ON p.user_id=u.user_id
                           WHERE
                                p.address LIKE %:address% AND
                                p.property_id NOT IN (
                                     SELECT property_id
                                     FROM bookings
                                     WHERE cheak_in<= :cheakIn AND cheak_out>= :cheakOut
                                );
                     """, nativeQuery = true)
       public List<Object[]> getProperties(@Param("address") String locationQueryString,
                     @Param("cheakIn") Date cheakIn,
                     @Param("cheakOut") Date cheakOut);

       @Query("""
                     SELECT new com.booking.application.dto.PropertyDetailDTO
                           (
                            p.propertyId,
                            p.user.username,
                            p.description,
                            p.city,
                            p.state,
                            p.country,
                            p.postal_code,
                            p.address,
                            p.price_per_night,
                            p.name,
                            p.heroImageSrc,
                            p.beds,
                            p.bedroom,
                            p.bathroom,
                            p.guests,
                            null
                          )
                     FROM PropertyEntity p
                     WHERE p.propertyId= :property_id
                     """)
       public PropertyDetailDTO getPropertyDetails(@Param("property_id") String property_id);

       @Query("""
                     SELECT new com.booking.application.dto.PropertyResponseDTO
                           (
                                 p.propertyId,
                                 p.user.username,
                                 p.city,
                                 p.heroImageSrc,
                                 p.price_per_night,
                                 p.name
                           )
                          FROM
                                PropertyEntity p
                          WHERE
                                p.user.username = :hostname
                          """)
       public List<PropertyResponseDTO> getHostProperties(@Param("hostname") String hostname);

}
