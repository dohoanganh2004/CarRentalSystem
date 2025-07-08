package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
@Data
public class ProfileRequestDTO implements Serializable {
    @NotBlank(message = "Please enter full name!")
    private String fullName;
    @NotBlank(message = "Please enter your phone number")
    private String phoneNumber;
    @NotBlank(message = "Please enter your phone number")
    private String nationalIDNo;
    @NotBlank(message = "Please enter your national")
    private String address;
    @NotNull(message = "Please enter your date of birth")
    private LocalDate birthDate;
    @NotBlank(message = "Please enter your email address")
    private String emailAddress;
    @NotBlank(message = "Please enter your driving license")
    private String drivingLicense;
}
