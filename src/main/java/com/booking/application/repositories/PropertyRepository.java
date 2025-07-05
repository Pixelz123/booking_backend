package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.application.entites.PropertyEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    
}
