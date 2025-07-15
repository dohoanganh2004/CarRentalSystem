package com.example.rentalcarsystem.dto.booking;

import com.example.rentalcarsystem.dto.UserInfoDTO;
import com.example.rentalcarsystem.model.Customer;
import com.example.rentalcarsystem.model.User;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
@Data
public class BookingInformationDTO implements Serializable {
    private BookingDetailsDTO bookingDetailsDTO;
    private String paymentMethod;
    private String status;
    private Integer carId;
}


