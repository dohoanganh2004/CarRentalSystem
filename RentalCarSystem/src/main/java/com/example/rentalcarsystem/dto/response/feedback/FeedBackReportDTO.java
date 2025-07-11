package com.example.rentalcarsystem.dto.response.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
@Data
@AllArgsConstructor
public class FeedBackReportDTO implements Serializable {
    private Double averageRating;
    private String userName;
    private String comment;
    private Instant feedbackDate;
    private String nameOfCar;
    private String frontImageUrl;
    private Instant from; // ngay muon
    private Instant to; //ngay tra

}
