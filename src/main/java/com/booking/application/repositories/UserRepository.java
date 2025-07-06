package com.booking.application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.application.entites.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
   public Optional<UserEntity> findByUsername(String username);
}
