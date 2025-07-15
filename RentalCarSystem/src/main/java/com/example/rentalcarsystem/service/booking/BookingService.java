package com.example.rentalcarsystem.service.booking;

import com.example.rentalcarsystem.dto.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.booking.FinishBookingDTO;
import com.example.rentalcarsystem.dto.response.rental.MyRentalResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    MyRentalResponseDTO listBookings(HttpServletRequest request);
   String updateBooking(Integer bookingId, BookingDetailsDTO bookingDetailsDTO);
   CarBookingDetailsDTO getBookingById(Integer bookingId);
   FinishBookingDTO rentalCar(BookingInformationDTO bookingInformationDTO, HttpServletRequest request);
}
