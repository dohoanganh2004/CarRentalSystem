package com.example.rentalcarsystem.dto.paymentHistory;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentHistoryDTO implements Serializable {
    private Integer paymentId;
    private BigDecimal paymentAmount;
    private String title;
    private Instant paymentDate;
    private Integer bookingId;
    private String carName;
    private String sender;
}


