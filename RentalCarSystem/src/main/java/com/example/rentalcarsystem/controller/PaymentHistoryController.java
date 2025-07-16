package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.service.paymetHistory.PaymentHistoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Controller of payment
 */
@RestController
@RequestMapping("/payment-manager")
public class PaymentHistoryController {

    private final PaymentHistoryServiceImpl paymentHistoryService;
    public PaymentHistoryController(PaymentHistoryServiceImpl paymentHistoryService) {
        this.paymentHistoryService = paymentHistoryService;
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
    public ResponseEntity<Page<PaymentHistoryDTO>> viewPaymentHistory(HttpServletRequest request,
                                                                      @RequestParam (required = false) Instant from,
                                                                      @RequestParam (required = false) Instant to,
                                                                      @RequestParam (defaultValue = "0")Integer page,
                                                                      @RequestParam (defaultValue = "5")Integer size) {
        Page<PaymentHistoryDTO> listPaymentHistory = paymentHistoryService.paymentHistory(request, from, to, page, size);
        return new ResponseEntity<>(listPaymentHistory, HttpStatus.OK);

    }

}
