package com.booking.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailDTO {
    private String property_id;
    private String username;
    private String description;
    private String city;
    private String state;
    private String country;
    private int postal_code;
    private String address;
    private double price_per_night;
    private List<String>imageList;
    private String name;
    private String hero_image_src;
}
