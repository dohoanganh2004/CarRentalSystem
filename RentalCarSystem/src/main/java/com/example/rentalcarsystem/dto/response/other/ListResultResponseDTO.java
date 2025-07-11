package com.example.rentalcarsystem.dto.response.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ListResultResponseDTO<T> {
    private String message;
    private List<T> data;


    public ListResultResponseDTO(String noResult, List<T> objects) {
    }
}
