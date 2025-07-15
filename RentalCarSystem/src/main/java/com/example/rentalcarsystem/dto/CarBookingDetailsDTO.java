package com.example.rentalcarsystem.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CarBookingDetailsDTO implements Serializable {
    private CarBookingBaseInfoDTO carBookingBaseInfoDTO;
    private UserInfoDTO customerInfoDTO;
    private UserInfoDTO carOwnerInfoDTO;
    private CarInformationDTO carInformationDTO;
    private String paymentMethod;

}
