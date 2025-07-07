package com.booking.application.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.application.dto.UserAuthRequestDTO;
import com.booking.application.dto.UserAuthResponseDTO;
import com.booking.application.entites.Role;
import com.booking.application.entites.UserEntity;
import com.booking.application.repositories.UserRepository;
import com.booking.application.services.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwt_service;
    @Autowired
    private UserRepository user_repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody UserAuthRequestDTO authRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));
        UserEntity user = (UserEntity) auth.getPrincipal();
        String token = jwt_service.createToken(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new UserAuthResponseDTO(
                                                                  user.getUsername(),
                                                                  token,
                                                                  user.getRoles()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@RequestBody UserAuthRequestDTO authRequest) {
        if (user_repo.findByUsername(authRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username taken\n");
        }
        UserEntity user=new UserEntity();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRoles(authRequest.getRoles() == null || authRequest.getRoles().isEmpty() ?
                      Set.of(Role.USER) : authRequest.getRoles());
        UserEntity new_user=user_repo.save(user);
        String token = jwt_service.createToken(new_user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new UserAuthResponseDTO(
                                                                     new_user.getUsername(),
                                                                     token,
                                                                     new_user.getRoles()));


    }

}
