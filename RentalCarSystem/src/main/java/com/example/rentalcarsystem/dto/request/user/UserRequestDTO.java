package com.example.rentalcarsystem.dto.request.user;

import com.example.rentalcarsystem.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
public class UserRequestDTO implements Serializable {

    private Integer id;


    private String fullName;


    private LocalDate dateOfBirth;


    private String nationalIDNo;


    private String phoneNo;


    private String email;


    private String password;


    private String address;


    private String drivingLicense;


    private BigDecimal wallet;


    private Integer roleId ;


    private Boolean status;
}
