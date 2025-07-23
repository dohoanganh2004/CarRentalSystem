package com.example.rentalcarsystem.service.user;

import com.example.rentalcarsystem.dto.request.user.ForgotPasswordDTO;
import com.example.rentalcarsystem.dto.request.user.ResetPasswordDTO;
import com.example.rentalcarsystem.dto.request.user.*;
import com.example.rentalcarsystem.dto.response.user.*;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.email.Email;
import com.example.rentalcarsystem.email.EmailService;
import com.example.rentalcarsystem.mapper.UserMapper;
import com.example.rentalcarsystem.model.Carowner;
import com.example.rentalcarsystem.model.Customer;
import com.example.rentalcarsystem.model.Role;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.CarOwnerRepository;
import com.example.rentalcarsystem.repository.CustomerRepository;
import com.example.rentalcarsystem.repository.RoleRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import com.example.rentalcarsystem.sercutiry.CustomUserDetails;

import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.specification.UserSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private final CustomerRepository customerRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final EmailService emailService;


    /**
     * Method to view all user of system
     * @param name name of user
     * @param email email of user
     * @param dateOfBirth date of birth of user
     * @param roleName role of user
     * @param address address of user

     * @param pageNo page user want to view
     * @param pageSize quantity of record display on screen

     * @param sortBy
     * @param sortOrder asc or desc
     * @return
     */
    @Override
    public Page<UserResponseDTO> findAll(String name, String email,String phoneNo, LocalDate dateOfBirth, String roleName,
                                         String address, Integer pageNo,
                                         Integer pageSize, String sortBy, String sortOrder) {

        Specification<User> specification = (root, query, cb) -> cb.conjunction();

        if(name != null && !name.isEmpty()){
        specification = specification.and(UserSpecification.usernameLike(name));
    }
    if(email != null && !email.isEmpty()){
        specification = specification.and(UserSpecification.emailLike(email));
    }
    if(dateOfBirth != null ) {
        specification = specification.and(UserSpecification.dateOfBirth(dateOfBirth));

    }
    if(roleName != null && !roleName.isEmpty()){
        specification = specification.and(UserSpecification.roleLike(roleName));
    }
    if(address != null && !address.isEmpty()){
        specification = specification.and(UserSpecification.addressLike(address));
    }
        Sort sort = Sort.by(sortBy);

        if(sortOrder.equals("asc")){
            sort = sort.ascending();
        }
        if(sortOrder.equals("desc")){

            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNo ,pageSize,sort);
        Page<User> listUser = userRepository.findAll(specification, pageable);
        return listUser.map(userMapper::toUserResponseDTO);
    }

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

        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);
        return new AuthResponseDTO(accessToken, refreshToken);


    }


    /**
     * Register
     *
     * @param registerRequestDTO
     * @return
     */
    @Transactional
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = userRepository.findUserByEmail((registerRequestDTO.getEmail()));
        if (user != null) {
            throw new RuntimeException("Email already existed. Please try another email.");
        }
        User registerUser = new User();
        registerUser.setFullName(registerRequestDTO.getFullName());
        registerUser.setEmail(registerRequestDTO.getEmail());
        registerUser.setPhoneNo(registerRequestDTO.getPhoneNo());
        String enterPassword = registerRequestDTO.getPassword();
        String confirmPassword = registerRequestDTO.getConfirmPassword();
        System.out.println("PhoneNo: " + registerRequestDTO.getPhoneNo());
        if (userRepository.existsByPhoneNo(registerRequestDTO.getPhoneNo())) {
            throw new RuntimeException("Phone already exists");
        }
        if (!enterPassword.equals(confirmPassword)) {
            throw new RuntimeException("“Password and Confirm password \n" +
                    "don’t match. Please try again.");
        } else {
            registerUser.setPassword(passwordEncoder.encode(enterPassword));
        }
        Role role = roleRepository.findById(registerRequestDTO.getRoleId()).orElseThrow(() -> new RuntimeException("Role id not found"));
        registerUser.setRole(role);

        if (!registerRequestDTO.isAgreeStatus()) {
            throw new RuntimeException("Agree status not set");
        }
        registerUser.setWallet(new BigDecimal(0));
        registerUser.setStatus(true);
        userRepository.save(registerUser);
        userRepository.flush();
        log.info("User with email " + registerRequestDTO.getEmail() + " registered successfully");
        System.out.println("PhoneNo:" + registerUser.getPhoneNo());
        System.out.println("id:" + registerUser.getId());

        if (registerRequestDTO.getRoleId() == 2) {
            Carowner carowner = new Carowner();
            carowner.setUser(registerUser);
            carOwnerRepository.save(carowner);
        }

        if (registerRequestDTO.getRoleId() == 3) {
            Customer customer = new Customer();
            customer.setUser(registerUser);
            customerRepository.save(customer);
        }

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
        if (userRepository.existsByEmailAndIdNot(profileRequestDTO.getEmailAddress(), id)) {
            throw new RuntimeException("User with email " + profileRequestDTO.getEmailAddress() + " already exists");
        }

        if (userRepository.existsByPhoneNoAndIdNot(profileRequestDTO.getPhoneNumber(), id)) {
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
     *
     * @param passwordRequestDTO
     * @return
     */
    @Override
    public PasswordResponseDTO password(PasswordRequestDTO passwordRequestDTO, Integer id) {
        User exisUser = userRepository.findUserById(id);
        if (exisUser == null) {
            throw new RuntimeException("User with id " + id + " not found");
        }
        String currentPassword = passwordEncoder.encode(exisUser.getPassword());
        if(!exisUser.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Current password is incorrect.");
        }
        String newPassword = passwordRequestDTO.getNewPassword();
        String confirmPassword = passwordRequestDTO.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and Confirm \n" +
                    "password don’t match");
        }
        System.out.println("enterPassword:" + newPassword);
        System.out.println("confirmPassword:" + confirmPassword);
        exisUser.setPassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
        userRepository.save(exisUser);
        return new PasswordResponseDTO("Password change successful!");
    }

    /**
     * Method to earn email from user want to reset password
     *
     * @param forgotPasswordDTO
     * @return
     */
    @Override
    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
            String email = forgotPasswordDTO.getEmail();
            System.out.println("email:" + email);
            User forgotPasswordUser = userRepository.findUserByEmail(email);


            if (forgotPasswordUser == null) {
                throw new RuntimeException("User not found!.Please try again");
            }
            sendEmailAfterResetPassword(email);
            String message = "If this email is exist, we'll send to email with link to reset password";
            return message;
        }

        /**
         * Method to reset password
         *
         * @param resetPasswordDTO
         * @return String a message to notification to user
         */
        @Override
        public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
            User resetPasswordUser = userRepository.findUserByEmail(resetPasswordDTO.getEmail());
            if (resetPasswordUser == null) {
                throw new RuntimeException("User not found!.Please try again");
            }
            String newPassword = resetPasswordDTO.getNewPassword();
            String confirmPassword = resetPasswordDTO.getConfirmPassword();
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match!");
            }
            resetPasswordUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(resetPasswordUser);
            String message = "Reset password successful";
            return message;
        }

        /**
         * Get wallet of user
         *
         * @param request
         * @return
         */
        @Override
        public WalletCurrentBalanceDTO getWalletCurrentBalanceOfUser(HttpServletRequest request) {
            String token = getTokenFromRequest(request);
            int userId = jwtTokenProvider.getUserIdFromToken(token);
            User user = userRepository.findUserById(userId);
            WalletCurrentBalanceDTO walletCurrentBalanceDTO = new WalletCurrentBalanceDTO();
            walletCurrentBalanceDTO.setBalance(user.getWallet());
            return walletCurrentBalanceDTO;
    }

    /**
     * Method to send email after reset password
     *
     * @param toEmail
     */
    public void sendEmailAfterResetPassword(String toEmail) {
        String subject = "Rent-a-car Password Reset";
        String body = "We have just received a password reset request for " + toEmail + ".\n" +
                "Please click here to reset your password. \n" +
                "For your security, the link will expire in 24 hours or immediately after you \n" +
                "reset your password.";
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject(subject);
        email.setBody(body);
        emailService.sendEmail(email);

    }


    /**
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);

        }
        return token;
    }

}
