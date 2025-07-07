package com.booking.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.application.dto.BookingRequestDTO;
import com.booking.application.dto.PropertyDetailDTO;
import com.booking.application.dto.PropertyRequestDTO;
import com.booking.application.dto.PropertyResponseDTO;
import com.booking.application.entites.BookingEntity;
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

    @PostMapping("/booking")
    public ResponseEntity<?> processBooking(@RequestBody BookingRequestDTO bookingRequest) {
        BookingEntity booking = booking_service.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/properties")
    public ResponseEntity<?> getProperties(@RequestBody PropertyRequestDTO propertyRequest) {
        List<PropertyResponseDTO> propertyList = property_service.getProperties(propertyRequest);
        return ResponseEntity.ok(propertyList);
    }

    @GetMapping("/propertyDetails/{property_id}")
    public ResponseEntity<?> getPropertyDetails(@PathVariable String property_id) {
        PropertyDetailDTO propertyDetail = property_service.getPropertyDetails(property_id);
        return ResponseEntity.ok(propertyDetail);
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/newProperty")
    public ResponseEntity<?> hostNewProperty(@RequestBody PropertyDetailDTO newProperty) {
        property_service.hostNewProperty(newProperty);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/hostProperties/{hostname}")
    public ResponseEntity<?> getHostProperty(@PathVariable String hostname) {
        List<PropertyResponseDTO> hostProperties = property_service.getHostProperties(hostname);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hostProperties);
    }

    @GetMapping("/test")
    public String test() {
        return "ok\n";
    }
}
