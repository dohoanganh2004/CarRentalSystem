package com.example.rentalcarsystem.dto.request.wallet;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WithdrawBalanceDTO implements Serializable {
    private BigDecimal withdrawalAmount;
}
