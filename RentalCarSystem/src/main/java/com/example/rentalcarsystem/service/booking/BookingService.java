package com.example.rentalcarsystem.service.booking;

import com.example.rentalcarsystem.dto.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.booking.BookingResultDTO;
import com.example.rentalcarsystem.dto.response.rental.MyRentalResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface BookingService {
    MyRentalResponseDTO listBookings(HttpServletRequest request);
   String updateBooking(Integer bookingId, BookingDetailsDTO bookingDetailsDTO);
   CarBookingDetailsDTO getBookingById(Integer bookingId);
   BookingResultDTO rentalCar(BookingInformationDTO bookingInformationDTO, HttpServletRequest request);
   CarBookingBaseInfoDTO cancelBooking(Integer bookingId,HttpServletRequest request);
   CarBookingBaseInfoDTO pickUpBooking(Integer bookingId, HttpServletRequest request);
   BookingResultDTO returnTheCar(Integer bookingId, HttpServletRequest request);
}
