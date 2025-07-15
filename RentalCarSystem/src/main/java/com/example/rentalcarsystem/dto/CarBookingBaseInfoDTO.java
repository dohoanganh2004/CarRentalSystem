package com.example.rentalcarsystem.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
@Data
public class CarBookingBaseInfoDTO implements Serializable {
    private String name;
    private Instant from;
    private Instant to;
    private long numberOfDays;
    private BigDecimal basePrice;
    private BigDecimal total;
    private BigDecimal deposit;
    private Integer bookingNo;
    private String bookingStatus;
    private String frontImageUrl;
    private String backImageUrl;
    private String leftImageUrl;
    private String rightImageUrl;
}
