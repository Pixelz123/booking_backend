package com.booking.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.application.entites.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
        
}
