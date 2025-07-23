package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
@Data

public class ForgotPasswordDTO implements Serializable {
    @NotBlank(message = "Please enter your email.")
    private String email;
}
