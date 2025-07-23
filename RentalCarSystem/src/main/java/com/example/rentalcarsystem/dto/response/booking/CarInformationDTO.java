package com.example.rentalcarsystem.dto.response.booking;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class CarInformationDTO implements Serializable {
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
    private String termsOfUse;
    private MultipartFile registrationPaper;
    private String registrationPaperUrl;

    private MultipartFile certificateOfInspection;
    private String certificateOfInspectionUrl;

    private MultipartFile insurance;
    private String insuranceUrl;







}
