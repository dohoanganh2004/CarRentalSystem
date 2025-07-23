package com.example.rentalcarsystem.service.token;

import com.example.rentalcarsystem.dto.request.token.RefreshTokenRequestDTO;
import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;
import com.example.rentalcarsystem.dto.response.token.TokenResponseDTO;
import com.example.rentalcarsystem.model.Blacklisttoken;
import com.example.rentalcarsystem.model.Refreshtoken;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.BlackListTokenRepository;
import com.example.rentalcarsystem.repository.RefreshTokenRepository;
import com.example.rentalcarsystem.sercutiry.CustomUserDetails;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
@Autowired
 private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Override
    public LogoutResponseDTO logout(String accessToken, String refreshToken) {
        Refreshtoken refreshtoken = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new RuntimeException("Refreshtoken not found")
        );

        refreshTokenRepository.delete(refreshtoken);
        Claims claims = jwtTokenProvider.extractClaimsFromToken(accessToken);
        Date expiration = claims.getExpiration();
        LocalDateTime expiryDate = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Blacklisttoken blacklisttoken = new Blacklisttoken();
        blacklisttoken.setToken(accessToken);
        blackListTokenRepository.save(blacklisttoken);

        return new LogoutResponseDTO("Logout successful") ;
    }

    /**
     * Lay lai acesstoken neu het han
     * @param refreshTokenRequestDTO
     * @return
     */
    @Override
    public TokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Refreshtoken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDTO.getRefreshToken()).orElseThrow(()->new RuntimeException("Refresh token not found"));
        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        User user = refreshToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new TokenResponseDTO(jwtTokenProvider.generateAccessToken(userDetails));

    }
}
