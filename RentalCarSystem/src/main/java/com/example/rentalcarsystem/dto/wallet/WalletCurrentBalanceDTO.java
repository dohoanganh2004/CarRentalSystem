package com.example.rentalcarsystem.dto.wallet;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
@Data
public class WalletCurrentBalanceDTO implements Serializable {
    private BigDecimal balance;
    public WalletCurrentBalanceDTO() {}
    public WalletCurrentBalanceDTO(BigDecimal totalBalance) {}


}
