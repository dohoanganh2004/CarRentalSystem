package com.example.rentalcarsystem.dto.booking;

import lombok.Data;

@Data
/**
 * Method to response message after customer booking a car
 */
public class BookingResultDTO {
    private String message;

    public BookingResultDTO() {
    }


    public BookingResultDTO(String message) {
        this.message = message;
    }
}
