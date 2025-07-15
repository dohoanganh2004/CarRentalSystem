package com.example.rentalcarsystem.dto.booking;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * Method to define
 */
@Data
public class BookingDetailsDTO implements Serializable {
    private String pickupLocation;
    private Instant pickupDateTime;
    private Instant dropDateTime;
}
