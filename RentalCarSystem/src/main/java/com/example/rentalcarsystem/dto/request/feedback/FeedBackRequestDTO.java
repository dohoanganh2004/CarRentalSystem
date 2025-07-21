package com.example.rentalcarsystem.dto.request.feedback;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
@Data
public class FeedBackRequestDTO implements Serializable {

    private Double rating;
    private String comment;
    private Integer bookId;
}
