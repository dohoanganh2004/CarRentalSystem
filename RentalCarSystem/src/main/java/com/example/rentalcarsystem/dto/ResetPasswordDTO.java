package com.example.rentalcarsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
@Data
public class ResetPasswordDTO implements Serializable {
    @NotBlank(message = "Please enter new password!")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$",
            message = "Password must be at least 7 characters long, contain at least one letter and one number."
    )
    private String newPassword;
    @NotBlank(message = "Please enter confirm password")
    private String confirmPassword;
}
