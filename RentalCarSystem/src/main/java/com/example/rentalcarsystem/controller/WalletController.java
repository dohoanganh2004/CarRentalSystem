package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.wallet.TopUpWalletDTO;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.dto.wallet.WithdrawBalanceDTO;
import com.example.rentalcarsystem.service.paymetHistory.PaymentHistoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wallet")
public class WalletController {

 private final PaymentHistoryServiceImpl paymentHistoryService;

 public WalletController(PaymentHistoryServiceImpl paymentHistoryService) {
     this.paymentHistoryService = paymentHistoryService;
 }
    @PostMapping("/topUp-balance")
    public ResponseEntity<WalletCurrentBalanceDTO> topUpWallet(HttpServletRequest request,
                                                              @RequestBody TopUpWalletDTO topUpWalletDTO) {

        return ResponseEntity.ok( paymentHistoryService.topUp(request, topUpWalletDTO));
    }

    @PostMapping("/withdraw-balance")
    public ResponseEntity<WalletCurrentBalanceDTO> withDrawWallet(HttpServletRequest request,
                                                                  @RequestBody WithdrawBalanceDTO withdrawBalanceDTO) {

        return ResponseEntity.ok( paymentHistoryService.withdraw(request, withdrawBalanceDTO));
    }
}
