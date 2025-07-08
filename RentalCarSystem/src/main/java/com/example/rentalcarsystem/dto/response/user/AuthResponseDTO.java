package com.example.rentalcarsystem.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class AuthResponseDTO implements Serializable {
    private String token;
}
