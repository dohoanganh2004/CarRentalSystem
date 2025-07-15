package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.model.Booking;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class BookingMapper {
    /**
     * Tranfer data from booking to CarBookingBaseInfoDTO
     * @param booking
     * @return
     */
    public CarBookingBaseInfoDTO toCarBookingBaseInfoDTO(Booking booking) {
        CarBookingBaseInfoDTO carBookingBaseInfoDTO = new CarBookingBaseInfoDTO();
        carBookingBaseInfoDTO.setName(booking.getCar().getName());
        carBookingBaseInfoDTO.setFrom(booking.getStartDateTime());
        carBookingBaseInfoDTO.setTo(booking.getEndDateTime());
        LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = booking.getEndDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
        carBookingBaseInfoDTO.setNumberOfDays(numberOfDays);
        BigDecimal basePrice = booking.getCar().getBasePrice();
        carBookingBaseInfoDTO.setBasePrice(basePrice);
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(numberOfDays));
        carBookingBaseInfoDTO.setTotal(totalPrice);
        BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015"))
                .setScale(2, RoundingMode.HALF_UP);
        carBookingBaseInfoDTO.setDeposit(deposit);
        carBookingBaseInfoDTO.setBookingStatus(booking.getStatus());
        carBookingBaseInfoDTO.setBookingNo(booking.getId());
        carBookingBaseInfoDTO.setFrontImageUrl(booking.getCar().getCarImage().getFrontImageUrl());
        carBookingBaseInfoDTO.setBackImageUrl(booking.getCar().getCarImage().getBackImageUrl());
        carBookingBaseInfoDTO.setLeftImageUrl(booking.getCar().getCarImage().getLeftImageUrl());
        carBookingBaseInfoDTO.setRightImageUrl(booking.getCar().getCarImage().getRightImageUrl());
        return carBookingBaseInfoDTO;
    }
}
