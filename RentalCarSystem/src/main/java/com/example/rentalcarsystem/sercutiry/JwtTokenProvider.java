package com.example.rentalcarsystem.sercutiry;

import com.example.rentalcarsystem.model.Refreshtoken;
import com.example.rentalcarsystem.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class  JwtTokenProvider {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtProperties jwtProperties;




    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        log.info(">>> JWT Signing Key (Base64): {}", base64Key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Create access token
     *
     * @param userDetails
     * @return
     */

    public String generateAccessToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());
        Map<String, String> claims = new HashMap<>();
        claims.put("role", userDetails.getUser().getRole().getId().toString());
        claims.put("role-name", userDetails.getUser().getRole().getRoleName());
        claims.put("email", userDetails.getUser().getEmail());
        claims.put("fullName", userDetails.getUser().getFullName());
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(Integer.toString(userDetails.getUser().getId()))

                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info(">>> JWT Token (Base64): {}", token);

        return token;
    }

    /**
     * Create refresh token
     * @param userDetails
     * @return
     */
    public String generateRefreshToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefresh());
        Map<String, String> claims = new HashMap<>();
        claims.put("role", userDetails.getUser().getRole().getId().toString());
        claims.put("role-name", userDetails.getUser().getRole().getRoleName());
        claims.put("email", userDetails.getUser().getEmail());
        claims.put("fullName", userDetails.getUser().getFullName());
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(Integer.toString(userDetails.getUser().getId()))

                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info(">>> JWT Token (Base64): {}", token);
        Refreshtoken refreshToken = new Refreshtoken();
        refreshToken.setUser(userDetails.getUser());
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate.toInstant());
        refreshToken.setIsRevoked(false);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    /**
     * Get user ID from token
     *
     * @param token
     * @return
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Get email from token
     *
     * @param token
     * @return
     */
    public String getEmailFromToken(String token) {
        Claims cliams = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(cliams.get("email"));
    }

    /**
     * Get role from token
     *
     * @param token
     * @return
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("role-name"));
    }

    /**
     * Get fullname from Token
     *
     * @param token
     * @return
     */
    public String getFullNameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("fullName"));
    }
    public Claims extractClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validate token
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


}
