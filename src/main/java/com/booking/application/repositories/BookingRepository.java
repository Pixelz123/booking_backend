package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.application.entites.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, String>{
    
}
