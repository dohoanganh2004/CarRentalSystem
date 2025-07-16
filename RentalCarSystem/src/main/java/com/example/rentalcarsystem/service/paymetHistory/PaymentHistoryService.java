package com.example.rentalcarsystem.service.paymetHistory;


import com.example.rentalcarsystem.dto.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.dto.wallet.TopUpWalletDTO;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.dto.wallet.WithdrawBalanceDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface PaymentHistoryService {
    Page<PaymentHistoryDTO> paymentHistory(HttpServletRequest httpServletRequest, Instant from
    , Instant to, Integer page, Integer size);
      WalletCurrentBalanceDTO topUp(HttpServletRequest request, TopUpWalletDTO topUpWalletDTO);
      WalletCurrentBalanceDTO withdraw(HttpServletRequest request, WithdrawBalanceDTO withdrawalDTO);

}
