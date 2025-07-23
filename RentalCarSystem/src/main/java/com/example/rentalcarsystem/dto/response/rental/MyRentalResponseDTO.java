package com.example.rentalcarsystem.dto.response.rental;

import com.example.rentalcarsystem.dto.response.booking.CarBookingBaseInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRentalResponseDTO {
   private String message;
   List<CarBookingBaseInfoDTO> list;
}
