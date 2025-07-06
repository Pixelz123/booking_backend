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
    private String user_id;
    private String description;
    private String city;
    private String state;
    private String country;
    private int postal_code;
    private String address;
    private double price_per_night;
    private List<String>ImageList;
}
