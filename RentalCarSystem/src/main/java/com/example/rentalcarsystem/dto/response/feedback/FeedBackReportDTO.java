package com.example.rentalcarsystem.dto.response.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data

public class FeedBackReportDTO implements Serializable {
    private Double averageRating;
   List<UserFeedBackDTO> userFeedBackDTO;

}
