package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.response.user.UserResponseDTO;
import com.example.rentalcarsystem.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/car-rental/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam (required = false) String name,
                                                             @RequestParam (required = false)String email,
                                                             @RequestParam (required = false) String phoneNo,
                                                             @RequestParam (required = false)LocalDate dateOfBirth,
                                                             @RequestParam (required = false)String roleName,
                                                             @RequestParam (required = false) String address,
                                                             @RequestParam (defaultValue = "0") Integer pageNo,
                                                             @RequestParam (defaultValue = "5") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortOrder) {


        return ResponseEntity.ok(userService.findAll(name, email, phoneNo, dateOfBirth, roleName, address, pageNo, pageSize, sortBy, sortOrder));
    }


}
