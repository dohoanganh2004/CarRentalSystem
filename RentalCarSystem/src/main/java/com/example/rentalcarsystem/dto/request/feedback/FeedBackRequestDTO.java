package com.example.rentalcarsystem.dto.request.feedback;

import lombok.Data;

import java.io.Serializable;
@Data
public class FeedBackRequestDTO implements Serializable {
    private Double rating;
    private String comment;
    private Integer bookId;
}
