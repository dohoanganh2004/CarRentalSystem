package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.model.PaymentHistory;
import org.springframework.stereotype.Component;

/**
 * Class include method to mapper between entity and DTO
 */
@Component
public class PaymentHistoryMapper {
    /**
     * Method to tranfer information from paymentHistory to PaymentHistoryDTO
     * @param paymentHistory entity
     * @return paymentHistoryDTO
     */
    public PaymentHistoryDTO toPaymentHistoryDTO(PaymentHistory paymentHistory) {
        PaymentHistoryDTO paymentHistoryDTO = new PaymentHistoryDTO();
        paymentHistoryDTO.setPaymentId(paymentHistory.getId());
        paymentHistoryDTO.setPaymentAmount(paymentHistory.getAmount());
        paymentHistoryDTO.setTitle(paymentHistory.getTitle());
        paymentHistoryDTO.setPaymentDate(paymentHistory.getPaymentDate());
        paymentHistoryDTO.setBookingId(paymentHistory.getBooking().getId());
        paymentHistoryDTO.setCarName(paymentHistory.getBooking().getCar().getName());
        paymentHistoryDTO.setSender(paymentHistory.getUser().getFullName());
        return paymentHistoryDTO;

    }
}
