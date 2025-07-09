package com.booking.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.application.dto.BookingRequestDTO;
import com.booking.application.dto.PropertyDetailDTO;
import com.booking.application.dto.PropertyResponseDTO;
import com.booking.application.repositories.BookingRepository;
import com.booking.application.services.BookingService;
import com.booking.application.services.PropertyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")

@RequiredArgsConstructor
public class ApiController {
    @Autowired
    private BookingService booking_service;
    @Autowired
    private PropertyService property_service;

    @Autowired
    private BookingRepository prop_repo;

    @PostMapping("/booking")
    public ResponseEntity<?> processBooking(@RequestBody BookingRequestDTO bookingRequest) {
        booking_service.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("BOOKING SUCESSFULL");
    }

  
   
    @GetMapping("/userBookings")
    public ResponseEntity<?> getUserBooking(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(prop_repo.getUserBookings(username));
    }

    @PreAuthorize("hasRole('HOST')")
    @PostMapping("/newProperty")
    public ResponseEntity<?> hostNewProperty(@RequestBody PropertyDetailDTO newProperty) {
        property_service.hostNewProperty(newProperty);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/hostProperties")
    public ResponseEntity<?> getHostProperty() {
        List<PropertyResponseDTO> hostProperties = property_service.getHostProperties();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hostProperties);
    }

    @GetMapping("/test")
    public String test() {
        return "ok\n";
    }
}
