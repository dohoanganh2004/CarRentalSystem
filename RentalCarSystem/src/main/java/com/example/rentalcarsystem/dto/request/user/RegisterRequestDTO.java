package com.example.rentalcarsystem.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
@Data
public class RegisterRequestDTO implements Serializable {
    @NotBlank(message = "Please enter full name!")
    private String fullName;
    @NotBlank(message = "Please enter your email address!")
    private String email;
    @NotBlank(message = "Please enter your phone number!")
    private String phoneNo;
    @NotBlank(message = "Please enter your password!")
    private String password;
    @NotBlank(message = "Please enter your confirm password!")
    private String confirmPassword;
    @NotNull(message = "Please choose the role")
    private Integer roleId;  // choose one of two option
    @NotNull(message = "Please tick the check box to process ! ")
    boolean agreeStatus;
}
