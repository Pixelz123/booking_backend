package com.booking.application.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.application.dto.BookingResponseDTO;
import com.booking.application.entites.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, String> {

        @Query(value = """
                            SELECT EXISTS (
                                SELECT 1 FROM bookings b
                                WHERE b.property_id = :property_id
                                  AND b.cheak_in < :checkOut
                                  AND b.cheak_out > :checkIn
                            )
                        """, nativeQuery = true)
        boolean existsOverlappingBookings(
                        @Param("property_id") String property_id,
                        @Param("checkIn") Date checkIn,
                        @Param("checkOut") Date checkOut);

        @Query("""
                            SELECT new com.booking.application.dto.BookingResponseDTO(
                                b.bookingId,
                                new com.booking.application.dto.PropertyResponseDTO(
                                    p.propertyId,
                                    u.username,
                                    p.city,
                                    p.heroImageSrc,
                                    p.price_per_night,
                                    p.name
                                ),
                                b.cheakIn,
                                b.cheakOut,
                                SIZE(b.guests),
                                b.totalprice
                            )
                            FROM BookingEntity b
                            JOIN b.property p
                            JOIN b.user u
                            WHERE u.username = :username
                        """)
        public List<BookingResponseDTO> getUserBookings(@Param("username") String username);
}
