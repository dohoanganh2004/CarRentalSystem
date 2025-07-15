package com.example.rentalcarsystem.dto.response.feedback;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
@Data

public class PublicCarFeedBackDTO implements Serializable {

    private String fullName;
    private String content;
    private Instant dateTime;
    private Double ratings;


    public PublicCarFeedBackDTO() {
    }

    public PublicCarFeedBackDTO(String fullName, String content, Instant dateTime, Double ratings) {
        this.fullName = fullName;
        this.content = content;
        this.dateTime = dateTime;
        this.ratings = ratings;
    }
}
