package com.example.rentalcarsystem.service.token;

import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;
import com.example.rentalcarsystem.model.Refreshtoken;
import com.example.rentalcarsystem.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
@Autowired
 private RefreshTokenRepository refreshTokenRepository;

    @Override
    public LogoutResponseDTO logout(String token) {
        Refreshtoken refreshtoken = refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Refreshtoken not found")
        );

        refreshTokenRepository.delete(refreshtoken);
        return new LogoutResponseDTO("Logout successful") ;
    }
}
