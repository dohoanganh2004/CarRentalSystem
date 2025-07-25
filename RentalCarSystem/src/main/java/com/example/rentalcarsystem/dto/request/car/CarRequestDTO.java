package com.example.rentalcarsystem.dto.request.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarRequestDTO implements Serializable {

    @NotBlank(message = "Please enter license plate!")
    private String licensePlate;
    @NotBlank(message = "Please enter color!")
    private String color;
    @NotBlank(message = "Please choose brand!")
    private String brandName;
    @NotBlank(message = "Please choose model!")
    private String model;

    private Integer productionYear;
    @Positive(message = "Number of seat must be positive number!")
    private Integer noOfSeats;
    @NotBlank(message = "Please choose transmission type!")
    private String transmission;
    @NotBlank(message = "Please choose fuel type!")
    private String fuel;
    @Positive(message = "Mileage must be positive number!")
    private Integer mileage;
    @Positive(message = "fuelConsumption must be positive number!")
    private BigDecimal fuelConsumption;
    @NotBlank(message = "Please choose city or province!")
    String cityOrProvince;
    @NotBlank(message = "Please choose district!")
    String district;
    @NotBlank(message = "Please choose ward!")
    String ward;
    @NotBlank(message = "Please enter house number or street!")
    String houseNumberOrStreet;
    @NotBlank(message = "Please enter description!")
    private String description;
    @NotBlank(message = "Please choose additional functions!")
    private String additionalFunctions;
    @Positive(message = "Base price must be positive number! ")
    private BigDecimal basePrice;
    @Positive(message = "Deposit must be positive number!")
    private BigDecimal deposit;
    @NotBlank(message = "Please choose term of use!")
    private String termsOfUse;

    private Boolean isActive;

    private MultipartFile registrationPaper;
    private String registrationPaperUrl;

    private MultipartFile certificateOfInspection;
    private String certificateOfInspectionUrl;

    private MultipartFile insurance;
    private String insuranceUrl;

    private MultipartFile frontImage;
    private String frontImageUrl;

    private MultipartFile backImage;
    private String backImageUrl;

    private MultipartFile rightImage;
    private String rightImageUrl;

    private MultipartFile leftImage;
    private String leftImageUrl;

}






