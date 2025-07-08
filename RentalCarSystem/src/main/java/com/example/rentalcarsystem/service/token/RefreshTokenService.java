package com.example.rentalcarsystem.service.token;

import com.example.rentalcarsystem.dto.request.token.LogoutRequestDTO;
import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;
import com.example.rentalcarsystem.model.Refreshtoken;

public interface RefreshTokenService {
 LogoutResponseDTO logout(String token);
}
