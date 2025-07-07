package com.booking.application.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.booking.application.entites.UserEntity;
import com.booking.application.services.JwtService;
import com.booking.application.services.UserService;

@Component
public class JwtAuthenticatorProvider implements AuthenticationProvider {
    @Autowired
    private JwtService jwt_services; 
    @Autowired
    private UserService user_services;
    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        String username = jwt_services.extractUsername(token);
        if (username == null) {
            throw new BadCredentialsException("User does not exist..\n");
        }
        UserEntity user = user_services.loadUserByUsername(username);
        if (!jwt_services.validateToken(token)|| user==null) {
            throw new BadCredentialsException("Invalid JWT token");
        }
        return new JwtAuthenticationToken(user, user.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }


}
