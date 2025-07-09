package com.example.rentalcarsystem.service.token;

import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;

public interface RefreshTokenService {
 LogoutResponseDTO logout(String token);
}
