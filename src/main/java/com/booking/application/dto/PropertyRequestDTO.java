package com.booking.application.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequestDTO {
    private String locationQueryString;
    private Date cheakIn;
    private Date cheakOut;
    private double minPrice;
    private double maxPrice;
}
