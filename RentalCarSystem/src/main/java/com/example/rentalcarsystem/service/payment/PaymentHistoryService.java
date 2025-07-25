package com.example.rentalcarsystem.service.payment;


import com.example.rentalcarsystem.dto.response.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.dto.request.wallet.TopUpWalletDTO;
import com.example.rentalcarsystem.dto.response.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.dto.request.wallet.WithdrawBalanceDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface PaymentHistoryService {
    Page<PaymentHistoryDTO> paymentHistory(HttpServletRequest httpServletRequest, Instant from
    , Instant to, Integer page, Integer size);
      WalletCurrentBalanceDTO topUp(HttpServletRequest request, TopUpWalletDTO topUpWalletDTO);
      WalletCurrentBalanceDTO withdraw(HttpServletRequest request, WithdrawBalanceDTO withdrawalDTO);

}
