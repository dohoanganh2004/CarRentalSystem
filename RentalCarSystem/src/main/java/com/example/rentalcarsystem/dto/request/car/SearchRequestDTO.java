package com.example.rentalcarsystem.dto.request.car;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class SearchRequestDTO {
    @NotBlank(message = "Please enter location!")
    private String pickUpLocation;
    @NotBlank(message = "Please enter pick-up date and time!")
    private Instant pickupDateTime ;
    @NotBlank(message = "Please enter drop-op date and time!")
    private Instant dropOffDateTime;
}
