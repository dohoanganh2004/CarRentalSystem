package com.example.rentalcarsystem.dto.response.user;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserResponseDTO implements Serializable {
    private int userId;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private String roleName;
    private String address;
    private boolean status;
}
