package com.example.rentalcarsystem.dto.response.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResultResponseDTO<T> {
    private String message;
    private List<T> data;



}
