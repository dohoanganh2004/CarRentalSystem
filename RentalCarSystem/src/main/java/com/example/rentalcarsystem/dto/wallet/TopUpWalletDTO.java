package com.example.rentalcarsystem.dto.wallet;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class TopUpWalletDTO implements Serializable {
    private BigDecimal topUpBalance;
}
