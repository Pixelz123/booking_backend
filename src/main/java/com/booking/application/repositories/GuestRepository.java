package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.application.entites.GuestEntity;

public interface GuestRepository extends JpaRepository<GuestEntity, String> {
        
}
