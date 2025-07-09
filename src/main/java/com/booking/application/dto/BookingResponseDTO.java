package com.booking.application.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO{
    private String bookingId;
    private PropertyResponseDTO property;
    private Date cheakIn;
    private Date cheakOut;
    private int guests;
    private double totalPrice;
}
