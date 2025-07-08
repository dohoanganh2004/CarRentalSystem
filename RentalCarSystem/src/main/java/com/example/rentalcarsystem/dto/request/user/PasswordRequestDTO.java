package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
@Data

public class PasswordRequestDTO implements Serializable {
    @NotBlank(message = "Please enter new password")
    private String newPassword;
    @NotBlank(message = "Please enter confirm password")
    private String confirmPassword;
}
