package com.example.rentalcarsystem.service.user;

import com.example.rentalcarsystem.dto.request.user.*;
import com.example.rentalcarsystem.dto.response.user.AuthResponseDTO;
import com.example.rentalcarsystem.dto.response.user.PasswordResponseDTO;
import com.example.rentalcarsystem.dto.response.user.ProfileResponseDTO;
import com.example.rentalcarsystem.dto.response.user.RegisterResponseDTO;
import com.example.rentalcarsystem.mapper.UserMapper;
import com.example.rentalcarsystem.model.Role;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.RoleRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import com.example.rentalcarsystem.sercutiry.CustomUserDetails;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;


    /**
     * Login
     *
     * @param authRequestDTO
     * @return
     */
    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getEmail(),
                        authRequestDTO.getPassword()
                )
        );
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(customUserDetails);
        return new AuthResponseDTO(token);


    }


    /**
     * Register
     *
     * @param registerRequestDTO
     * @return
     */
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = userRepository.findUserByEmail((registerRequestDTO.getEmail()));
        if (user != null) {
            throw new RuntimeException("User with email " + registerRequestDTO.getEmail() + " already exists");
        }
        User registerUser = new User();
        registerUser.setFullName(registerRequestDTO.getFullName());
        registerUser.setEmail(registerRequestDTO.getEmail());
        registerUser.setPhoneNo(registerRequestDTO.getPhoneNo());
        String enterPassword = registerRequestDTO.getPassword();
        String confirmPassword = registerRequestDTO.getConfirmPassword();
        System.out.println("PhoneNo: " + registerRequestDTO.getPhoneNo());
        if (userRepository.existsByEmailAndPhoneNo(registerRequestDTO.getEmail(), registerRequestDTO.getPhoneNo())) {
            throw new RuntimeException("User already exists");
        }
        if (!enterPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        } else {
            registerUser.setPassword(passwordEncoder.encode(enterPassword));
        }
        Role role = roleRepository.findById(registerRequestDTO.getRoleId()).orElseThrow(() -> new RuntimeException("Role id not found"));
        registerUser.setRole(role);

        if (!registerRequestDTO.isAgreeStatus()) {
            throw new RuntimeException("Agree status not set");
        }
        userRepository.save(registerUser);
        log.info("User with email " + registerRequestDTO.getEmail() + " registered successfully");
        System.out.println("PhoneNo:" + registerUser.getPhoneNo());
        return userMapper.toRegisterResponseDTO(registerUser);
    }

    /**
     * Change profile
     *
     * @param profileRequestDTO
     * @param id
     * @return
     */
    @Override
    public ProfileResponseDTO profile(ProfileRequestDTO profileRequestDTO, Integer id) {
        User exisUser = userRepository.findUserById(id);
        if (exisUser == null) {
            throw new RuntimeException("User with id " + id + " not found");
        }
        if(userRepository.existsByEmailAndIdNot(profileRequestDTO.getEmailAddress(), id)) {
         throw new RuntimeException("User with email " + profileRequestDTO.getEmailAddress() + " already exists");
        }

        if(userRepository.existsByPhoneNoAndIdNot(profileRequestDTO.getPhoneNumber(),id)) {
            throw new RuntimeException("User with phone " + profileRequestDTO.getEmailAddress() + " already exists");
        }


            exisUser.setFullName(profileRequestDTO.getFullName());
            exisUser.setPhoneNo(profileRequestDTO.getPhoneNumber());
            exisUser.setNationalIDNo(profileRequestDTO.getNationalIDNo());
            exisUser.setAddress(profileRequestDTO.getAddress());
            exisUser.setDateOfBirth(profileRequestDTO.getBirthDate());
            exisUser.setEmail(profileRequestDTO.getEmailAddress());
            exisUser.setDrivingLicense(profileRequestDTO.getDrivingLicense());
            System.out.println("PhoneNo:" + exisUser.getPhoneNo());
            userRepository.save(exisUser);

            ProfileResponseDTO profileResponseDTO = userMapper.toProfileResponseDTO(exisUser);
            System.out.println("PhoneNo:" + profileResponseDTO.getPhoneNumber());
            return profileResponseDTO;
        }


    /**
     * Change password
     * @param passwordRequestDTO
     * @return
     */
    @Override
    public PasswordResponseDTO password(PasswordRequestDTO passwordRequestDTO , Integer id) {
        User exisUser = userRepository.findUserById(id);
        if(exisUser == null) {
            throw new RuntimeException("User with id " + id + " not found");
        }
        String newPassword  = passwordRequestDTO.getNewPassword();
        String confirmPassword = passwordRequestDTO.getConfirmPassword();

        if(!newPassword.equals(confirmPassword)){
            throw new RuntimeException("Passwords do not match");
        }
        System.out.println("enterPassword:" +newPassword);
        System.out.println("confirmPassword:" +confirmPassword);
        exisUser.setPassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
        userRepository.save(exisUser);
        return new PasswordResponseDTO("Password change sucessful");
    }


}
