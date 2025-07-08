package com.example.rentalcarsystem.exception;

import lombok.Data;

@Data
public class BaseErrorResponse {
    private String code;
    private String status;
}
