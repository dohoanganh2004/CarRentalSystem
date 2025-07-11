package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "UserID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Size(max = 20)
    @Column(name = "NationalIDNo", length = 20)
    private String nationalIDNo;

    @Size(max = 20)
    @Column(name = "PhoneNo", length = 20)
    private String phoneNo;

    @Size(max = 100)
    @NotNull
    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(max = 255)
    @Column(name = "Address")
    private String address;

    @Size(max = 50)
    @Column(name = "DrivingLicense", length = 50)
    private String drivingLicense;

    @ColumnDefault("0.00")
    @Column(name = "Wallet", precision = 10, scale = 2)
    private BigDecimal wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleID")
    private Role role;

    @ColumnDefault("1")
    @Column(name = "Status")
    private Boolean status;

}