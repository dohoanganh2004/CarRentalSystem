package com.example.rentalcarsystem.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
public class UserRequestDTO implements Serializable {

    private Integer id;


    private String fullName;


    private LocalDate dateOfBirth;


    private String nationalIDNo;


    private String phoneNo;


    private String email;


    private String password;


    private String address;


    private String drivingLicense;


    private BigDecimal wallet;


    private Integer roleId ;


    private Boolean status;
}
