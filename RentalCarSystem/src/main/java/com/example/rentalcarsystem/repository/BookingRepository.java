package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Booking;
import com.example.rentalcarsystem.model.Car;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


    List<Booking> findByCarId(Integer carId);
    boolean existsByCarId(Integer carId);

    Booking findBookingById(Integer id);

    List<Booking> findBookingByCustomer_Id(Integer customerId);

    Car findBookingByCarId(Integer carId);

    boolean existsByCarIdAndStartDateTimeAndEndDateTimeAndStatusNot(Integer carId, Instant startDateTime, Instant endDateTime, @Size(max = 50) String status);
}
