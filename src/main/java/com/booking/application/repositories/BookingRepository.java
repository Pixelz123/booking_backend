package com.booking.application.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.application.entites.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, String> {

    // boolean bookingExistByPropertyAndDates(String propertyId,Date cheakIn,Date
    // cheakOut);
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
}
