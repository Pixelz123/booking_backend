package com.booking.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private BookingService booking_service;
    @Autowired
    private PropertyService property_service;

    @PostMapping("/booking")
    public ResponseEntity<?> processBooking(@RequestBody BookingRequestDTO bookingRequest){
        BookingEntity booking=booking_service.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }
    @PostMapping("/properties")
    public ResponseEntity<?> getProperties(@RequestBody PropertyRequestDTO propertyRequest){
        List<PropertyResponseDTO> propertyList=property_service.getProperties(propertyRequest);
        return ResponseEntity.ok(propertyList);
    }
    @GetMapping("/propertyDetails/{property_id}")
    public ResponseEntity<?> getPropertyDetails(@PathVariable String property_id){
        PropertyDetailDTO propertyDetail=property_service.getPropertyDetails(property_id);
        return ResponseEntity.ok(propertyDetail);
    }
    @GetMapping("/test")
    public String test(){
        return "ok\n";
    }
}
