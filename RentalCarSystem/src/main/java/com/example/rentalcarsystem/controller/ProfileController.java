package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.user.PasswordRequestDTO;
import com.example.rentalcarsystem.dto.request.user.ProfileRequestDTO;
import com.example.rentalcarsystem.dto.response.user.PasswordResponseDTO;
import com.example.rentalcarsystem.dto.response.user.ProfileResponseDTO;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car-rental/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PutMapping("/change-profile")
    public ResponseEntity<ProfileResponseDTO> profile(@RequestBody @Valid ProfileRequestDTO profileRequestDTO,
                                                      HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        System.out.println("user id test:" + userId);
        return new ResponseEntity<>(userService.profile(profileRequestDTO, userId), HttpStatus.OK);
    }


    @PutMapping ("/change-password")
    public ResponseEntity<PasswordResponseDTO> changePassword(@RequestBody @Valid PasswordRequestDTO passwordRequestDTO,
                                                              HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);

        return new ResponseEntity<>(userService.password(passwordRequestDTO, userId), HttpStatus.OK);
    }


    /**
     * Get token from request
     *
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }

}
