package com.example.rentalcarsystem.service.user;

import com.example.rentalcarsystem.dto.request.user.ForgotPasswordDTO;
import com.example.rentalcarsystem.dto.request.user.ResetPasswordDTO;
import com.example.rentalcarsystem.dto.request.user.AuthRequestDTO;
import com.example.rentalcarsystem.dto.request.user.PasswordRequestDTO;
import com.example.rentalcarsystem.dto.request.user.ProfileRequestDTO;
import com.example.rentalcarsystem.dto.request.user.RegisterRequestDTO;
import com.example.rentalcarsystem.dto.response.user.*;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface UserService {
    Page<UserResponseDTO> findAll(String name , String email, String phoneNo,LocalDate dateOfBirth, String roleName, String address, Integer pageNo,
                                  Integer pageSize, String sortBy, String sortOrder);
    AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO);
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
     ProfileResponseDTO profile(ProfileRequestDTO profileRequestDTO ,Integer id);
     PasswordResponseDTO password(PasswordRequestDTO passwordRequestDTO , Integer id);
     String forgotPassword (ForgotPasswordDTO forgotPasswordDTO);
     String resetPassword (ResetPasswordDTO resetPasswordDTO);
     WalletCurrentBalanceDTO getWalletCurrentBalanceOfUser(HttpServletRequest request);
}
