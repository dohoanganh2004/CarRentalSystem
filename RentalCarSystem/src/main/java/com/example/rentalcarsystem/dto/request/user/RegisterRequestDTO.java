package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
@Data
public class RegisterRequestDTO implements Serializable {
    @NotBlank(message = "Please enter full name!")
    private String fullName;
    @NotBlank(message = "Please enter your email address!")
    @Email(message = "Please enter available email.")
    private String email;
    @NotBlank(message = "Please enter your phone number!")
    private String phoneNo;
    @NotBlank(message = "Please enter your password!")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$",
            message = "Password must be at least 7 characters long, contain at least one letter and one number."
    )
    private String password;

    @NotBlank(message = "Please enter your confirm password!")
    private String confirmPassword;
    @NotNull(message = "Please choose the role")
    private Integer roleId;  // choose one of two option
    @NotNull(message = "Please tick the check box to process ! ")
    boolean agreeStatus;
}
