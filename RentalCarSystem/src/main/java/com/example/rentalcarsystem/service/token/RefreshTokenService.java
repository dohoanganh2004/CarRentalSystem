package com.example.rentalcarsystem.service.token;

import com.example.rentalcarsystem.dto.request.token.RefreshTokenRequestDTO;
import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;
import com.example.rentalcarsystem.dto.response.token.TokenResponseDTO;

public interface RefreshTokenService {
 LogoutResponseDTO logout(String acessToken, String refreshToken);
 TokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}
