package com.booking.application.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingRequestDTO {
    private String username;
    private String propertyId;
    private List<GuestDTO> guestList;
    private Date cheakIn;
    private Date cheakOut;
}
