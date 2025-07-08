package com.example.rentalcarsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
@Data
@AllArgsConstructor
public class ValidatorErrorResponse extends BaseErrorResponse {
    private Map<String, String> errors;




}
