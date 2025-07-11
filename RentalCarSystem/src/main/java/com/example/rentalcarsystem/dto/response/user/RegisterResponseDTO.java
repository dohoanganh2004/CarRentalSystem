package com.example.rentalcarsystem.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RegisterResponseDTO implements Serializable {

    private String fullName;
    private String email;
    private String phoneNo;
    private String roleName;

}
