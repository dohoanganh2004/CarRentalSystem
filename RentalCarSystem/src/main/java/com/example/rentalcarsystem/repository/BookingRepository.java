package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


    List<Booking> findByCarId(Integer carId);
    boolean existsByCarId(Integer carId);
}
