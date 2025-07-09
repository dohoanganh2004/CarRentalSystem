package com.example.rentalcarsystem.dto.request.car;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class SearchRequestDTO {
    private String pickUpLocation;
    private LocalDateTime pickupDateTime ;
    private LocalDateTime dropOffDateTime;
}
