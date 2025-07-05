package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.application.entites.PropertyEntity;

public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    
}
