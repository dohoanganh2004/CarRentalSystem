package com.example.rentalcarsystem.dto.response.booking;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarBookingDetailsDTO implements Serializable {
    private CarBookingBaseInfoDTO carBookingBaseInfoDTO;
    private UserInfoDTO customerInfoDTO;
    private UserInfoDTO carOwnerInfoDTO;
    private CarInformationDTO carInformationDTO;
    private String paymentMethod;
    private BigDecimal wallet;

}
