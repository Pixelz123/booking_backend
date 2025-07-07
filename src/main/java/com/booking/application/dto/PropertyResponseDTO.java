package com.booking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponseDTO {
    private String propertyId;
    private String hostname;
    private String city;
    private String heroImageSrc;
    private double price_per_night; 
    private String name;
}
