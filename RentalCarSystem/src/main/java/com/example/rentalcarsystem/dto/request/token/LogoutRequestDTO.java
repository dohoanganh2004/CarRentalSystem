package com.example.rentalcarsystem.dto.request.token;

import lombok.Data;

import java.io.Serializable;
@Data
public class LogoutRequestDTO implements Serializable {
    private String token;
}
