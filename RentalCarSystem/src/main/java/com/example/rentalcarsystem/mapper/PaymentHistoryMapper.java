package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.response.paymentHistory.PaymentHistoryDTO;
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

        if (paymentHistory.getBooking() != null) {
            paymentHistoryDTO.setBookingId(paymentHistory.getBooking().getId());

            if (paymentHistory.getBooking().getCar() != null) {
                paymentHistoryDTO.setCarName(paymentHistory.getBooking().getCar().getName());
            } else {
                paymentHistoryDTO.setCarName("N/A");
            }
        } else {
            paymentHistoryDTO.setBookingId(null);
            paymentHistoryDTO.setCarName("N/A");
        }

        if (paymentHistory.getUser() != null) {
            paymentHistoryDTO.setUser(paymentHistory.getUser().getFullName());
        } else {
            paymentHistoryDTO.setUser("N/A");
        }

        return paymentHistoryDTO;
    }

}
