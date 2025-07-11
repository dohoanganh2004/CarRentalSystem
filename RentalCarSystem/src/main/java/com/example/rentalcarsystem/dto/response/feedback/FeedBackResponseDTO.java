package com.example.rentalcarsystem.dto.response.feedback;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class FeedBackResponseDTO {
    private String status;

    public FeedBackResponseDTO(String status) {
        this.status = status;
    }
}
