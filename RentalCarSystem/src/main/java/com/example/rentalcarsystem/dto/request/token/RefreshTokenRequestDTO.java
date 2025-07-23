package com.example.rentalcarsystem.dto.request.token;

import lombok.Data;

import java.io.Serializable;
@Data
public class RefreshTokenRequestDTO implements Serializable {
    private String refreshToken;
}
