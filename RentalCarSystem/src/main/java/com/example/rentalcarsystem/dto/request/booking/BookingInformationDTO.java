package com.example.rentalcarsystem.dto.request.booking;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * Method to
 */
public class BookingInformationDTO implements Serializable {
    private BookingDetailsDTO bookingDetailsDTO;
    private String paymentMethod;
    private Integer carId;

}


