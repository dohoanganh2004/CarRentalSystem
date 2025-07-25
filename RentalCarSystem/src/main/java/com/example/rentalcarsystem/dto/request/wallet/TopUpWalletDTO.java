package com.example.rentalcarsystem.dto.request.wallet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class TopUpWalletDTO implements Serializable {
    @NotNull(message = "Please enter money you want top up.")
    @Positive(message = "Please enter positive number")
    private BigDecimal topUpBalance;
}
