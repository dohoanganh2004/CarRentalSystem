package com.example.rentalcarsystem.dto.response.car;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SearchCarResponseDTO implements Serializable {
private String name;
private String frontImageUrl;
private String backImageUrl;
private String rightImageUrl;
private String leftImageUrl;
private Integer rating;
private BigDecimal noOfRides;
private BigDecimal price;
private String location;
private String status;

}
