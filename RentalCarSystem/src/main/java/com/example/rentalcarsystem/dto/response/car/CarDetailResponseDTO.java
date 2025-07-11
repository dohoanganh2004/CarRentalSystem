package com.example.rentalcarsystem.dto.response.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarDetailResponseDTO implements Serializable {


    private String licensePlate;

    private String color;

    private String brandName;

    private String model;

    private Integer productionYear;

    private Integer noOfSeats;

    private String transmission;

    private String fuel;

    private Integer mileage;

    private BigDecimal fuelConsumption;
    private String address;

    private String description;

    private String additionalFunctions;

    private BigDecimal basePrice;

    private BigDecimal deposit;

    private String termsOfUse;


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
