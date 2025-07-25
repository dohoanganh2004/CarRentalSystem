package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.response.other.PagingResponse;
import com.example.rentalcarsystem.dto.response.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.dto.request.wallet.TopUpWalletDTO;
import com.example.rentalcarsystem.dto.response.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.dto.request.wallet.WithdrawBalanceDTO;
import com.example.rentalcarsystem.service.payment.PaymentHistoryServiceImpl;
import com.example.rentalcarsystem.service.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Controller of payment
 */
@RestController
@RequestMapping("/car-rental/payment-manage")
public class PaymentController {

    private final PaymentHistoryServiceImpl paymentHistoryService;
    private final UserServiceImpl userService;
    public PaymentController(PaymentHistoryServiceImpl paymentHistoryService,
                             UserServiceImpl userService) {
        this.paymentHistoryService = paymentHistoryService;
        this.userService = userService;
    }

    /**
     * Method to recieve request freom client
     * @param request
     * @param from
     * @param to
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/history")
    public ResponseEntity<?> viewPaymentHistory(HttpServletRequest request,
                                                                      @RequestParam (required = false) Instant from,
                                                                      @RequestParam (required = false) Instant to,
                                                                      @RequestParam (defaultValue = "0")Integer page,
                                                                      @RequestParam (defaultValue = "5")Integer size) {
        Page<PaymentHistoryDTO> listPaymentHistory = paymentHistoryService.paymentHistory(request, from, to, page, size);
        return  ResponseEntity.ok(new PagingResponse<>(listPaymentHistory));

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

    @GetMapping("/my-wallet")
    public ResponseEntity<WalletCurrentBalanceDTO> myWalletBalance(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getWalletCurrentBalanceOfUser(request), HttpStatus.OK);
    }

}
