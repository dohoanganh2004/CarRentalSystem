package com.example.rentalcarsystem.dto.response.car;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CarResponseDTO implements Serializable {

    private String name;
    private String frontImageUrl;
    private String backImageUrl;
    private String rightImageUrl;
    private String leftImageUrl;
    private Double rating;
    private Integer noOfRides;
    private BigDecimal price;
    private String location;
    private String status;


    public CarResponseDTO() {

    }
}
