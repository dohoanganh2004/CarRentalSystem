package com.example.rentalcarsystem.service.user;

import com.example.rentalcarsystem.dto.ForgotPasswordDTO;
import com.example.rentalcarsystem.dto.ResetPasswordDTO;
import com.example.rentalcarsystem.dto.request.user.AuthRequestDTO;
import com.example.rentalcarsystem.dto.request.user.PasswordRequestDTO;
import com.example.rentalcarsystem.dto.request.user.ProfileRequestDTO;
import com.example.rentalcarsystem.dto.request.user.RegisterRequestDTO;
import com.example.rentalcarsystem.dto.response.user.AuthResponseDTO;
import com.example.rentalcarsystem.dto.response.user.PasswordResponseDTO;
import com.example.rentalcarsystem.dto.response.user.ProfileResponseDTO;
import com.example.rentalcarsystem.dto.response.user.RegisterResponseDTO;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO);
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
     ProfileResponseDTO profile(ProfileRequestDTO profileRequestDTO ,Integer id);
     PasswordResponseDTO password(PasswordRequestDTO passwordRequestDTO , Integer id);
     String forgotPassword (ForgotPasswordDTO forgotPasswordDTO);
     String resetPassword (ResetPasswordDTO resetPasswordDTO);
     WalletCurrentBalanceDTO getWalletCurrentBalanceOfUser(HttpServletRequest request);
}
