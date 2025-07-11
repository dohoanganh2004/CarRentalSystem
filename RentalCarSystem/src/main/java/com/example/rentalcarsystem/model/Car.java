package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "CarID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CarOwnerID")
    private Carowner carOwner;

    @Size(max = 100)
    @Column(name = "Name", length = 100)
    private String name;

    @Size(max = 20)
    @Column(name = "LicensePlate", length = 20)
    private String licensePlate;

    @Size(max = 50)
    @Column(name = "Brand", length = 50)
    private String brand;

    @Size(max = 50)
    @Column(name = "Model", length = 50)
    private String model;

    @Size(max = 30)
    @Column(name = "Color", length = 30)
    private String color;

    @Column(name = "NumberOfSeats")
    private Integer numberOfSeats;

    @Column(name = "ProductionYears")
    private Integer productionYears;

    @Size(max = 50)
    @Column(name = "TransmissionType", length = 50)
    private String transmissionType;

    @Size(max = 50)
    @Column(name = "FuelType", length = 50)
    private String fuelType;

    @Column(name = "Mileage")
    private Integer mileage;

    @Column(name = "FuelConsumption", precision = 5, scale = 2)
    private BigDecimal fuelConsumption;

    @Column(name = "BasePrice", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "Deposit", precision = 10, scale = 2)
    private BigDecimal deposit;

    @Size(max = 255)
    @Column(name = "Address")
    private String address;

    @Lob
    @Column(name = "Description")
    private String description;

    @Lob
    @Column(name = "AdditionalFunctions")
    private String additionalFunctions;

    @Lob
    @Column(name = "TermsOfUse")
    private String termsOfUse;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "CarImageID")
    private Carimage carImage;

    @Size(max = 50)
    @Column(name = "Status", length = 50)
    private String status;

    @Size(max = 255)
    @Column(name = "RegistrationPaperUrl")
    private String registrationPaperUrl;

    @Size(max = 255)
    @Column(name = "CertificateOfInspectionUrl")
    private String certificateOfInspectionUrl;

    @Size(max = 255)
    @Column(name = "InsuranceUrl")
    private String insuranceUrl;

}