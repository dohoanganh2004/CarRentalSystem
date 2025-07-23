package com.example.rentalcarsystem.service.booking;

import com.example.rentalcarsystem.dto.response.booking.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.response.booking.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.request.booking.BookingDetailsDTO;
import com.example.rentalcarsystem.dto.request.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.response.booking.BookingResultDTO;
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
   String confirmDeposit (HttpServletRequest request,Integer bookingId);
   String confirmPayment (HttpServletRequest request,Integer bookingId);
}
