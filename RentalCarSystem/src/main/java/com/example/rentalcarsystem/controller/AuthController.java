package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.token.LogoutRequestDTO;
import com.example.rentalcarsystem.dto.request.user.AuthRequestDTO;
import com.example.rentalcarsystem.dto.request.user.PasswordRequestDTO;
import com.example.rentalcarsystem.dto.request.user.ProfileRequestDTO;
import com.example.rentalcarsystem.dto.request.user.RegisterRequestDTO;
import com.example.rentalcarsystem.dto.response.token.LogoutResponseDTO;
import com.example.rentalcarsystem.dto.response.user.AuthResponseDTO;
import com.example.rentalcarsystem.dto.response.user.PasswordResponseDTO;
import com.example.rentalcarsystem.dto.response.user.ProfileResponseDTO;
import com.example.rentalcarsystem.dto.response.user.RegisterResponseDTO;
import com.example.rentalcarsystem.model.Refreshtoken;
import com.example.rentalcarsystem.repository.RefreshTokenRepository;
import com.example.rentalcarsystem.service.token.RefreshTokenService;
import com.example.rentalcarsystem.service.token.RefreshTokenServiceImpl;
import com.example.rentalcarsystem.service.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody @Valid AuthRequestDTO authRequestDTO) {
        return new ResponseEntity<>(userService.authenticate(authRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register (@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        //log.info("Controller da duoc goi den");
        return new ResponseEntity<>(userService.register(registerRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/logout")
     public ResponseEntity<LogoutResponseDTO> logout (@RequestBody LogoutRequestDTO logoutRequestDTO) {
        System.out.println(logoutRequestDTO.getToken());
        LogoutResponseDTO logoutResponseDTO = refreshTokenService.logout(logoutRequestDTO.getToken());
        return new ResponseEntity<>(logoutResponseDTO, HttpStatus.OK);
     }




}
