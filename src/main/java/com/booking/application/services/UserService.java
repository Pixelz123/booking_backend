package com.booking.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.application.entites.UserEntity;
import com.booking.application.repositories.UserRepository;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    public UserRepository user_repo; 
    @Override
    @Transactional(readOnly=true)
    public UserEntity loadUserByUsername(String username){
     return user_repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
