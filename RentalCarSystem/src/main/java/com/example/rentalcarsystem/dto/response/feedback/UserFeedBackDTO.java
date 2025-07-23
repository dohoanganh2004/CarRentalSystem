package com.example.rentalcarsystem.dto.response.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFeedBackDTO implements Serializable {
    private String fullName;
    private String content;
    private Double rating;
    private Instant feedbackDate;
    private String nameOfCar;
    private String frontImageUrl;
    private Instant from; // ngay muon
    private Instant to; //ngay tra
}
