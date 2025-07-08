package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
@Data

public class AuthRequestDTO implements Serializable {
    @NotBlank(message = "Please enter your email!")
    private String email;
    @NotBlank(message = "Please enter your password!")
    private String password;
}
