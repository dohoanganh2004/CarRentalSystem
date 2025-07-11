package com.example.rentalcarsystem.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PasswordResponseDTO implements Serializable {
    private String message;
}
