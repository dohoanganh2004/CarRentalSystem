package com.example.rentalcarsystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class FinishBookingDTO {
    private  String message;

    public FinishBookingDTO() {
    }


    public FinishBookingDTO(String message) {
        this.message = message;
    }
}
