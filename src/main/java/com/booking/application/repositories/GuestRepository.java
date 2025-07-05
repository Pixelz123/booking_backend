package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.application.entites.GuestEntity;
@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, String> {
        
}
