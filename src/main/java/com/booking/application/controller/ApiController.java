package com.booking.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.application.dto.BookingRequestDTO;
import com.booking.application.entites.BookingEntity;
import com.booking.application.services.BookingService;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private BookingService booking_service;

    @PostMapping("/booking")
    public ResponseEntity<?> processBooking(@RequestBody BookingRequestDTO bookingRequest){
        BookingEntity booking=booking_service.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }
}
