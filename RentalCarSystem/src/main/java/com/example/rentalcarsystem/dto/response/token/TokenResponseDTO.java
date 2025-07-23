package com.example.rentalcarsystem.dto.response.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class TokenResponseDTO implements Serializable {
    private String accessToken;
}
