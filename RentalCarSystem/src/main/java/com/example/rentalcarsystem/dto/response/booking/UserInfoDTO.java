package com.example.rentalcarsystem.dto.response.booking;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserInfoDTO implements Serializable {
    private String fullName;
    private String phone;
    private String nationalIdNo;
    private LocalDate dateOfBirth;
    private String email;
    private String drivingLicense;
    String cityOrProvince;
    String district;
    String ward;
    String houseNumberOrStreet;

}



