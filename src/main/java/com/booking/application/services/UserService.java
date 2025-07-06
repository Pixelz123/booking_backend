package com.booking.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.booking.application.entites.UserEntity;
import com.booking.application.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    public UserRepository user_repo; 
    @Override
    public UserEntity loadUserByUsername(String username){
       UserEntity user=user_repo.findByUsername(username).get();
       return user;
    }
}
