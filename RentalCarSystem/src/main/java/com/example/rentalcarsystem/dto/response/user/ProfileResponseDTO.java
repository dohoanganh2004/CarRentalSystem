package com.example.rentalcarsystem.dto.response.user;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ProfileResponseDTO implements Serializable {
    private String fullName;
    private String phoneNumber;
    private String nationalIDNo;
    private String address;
    private LocalDate birthDate;
    private String emailAddress;
    private String drivingLicense;
}
