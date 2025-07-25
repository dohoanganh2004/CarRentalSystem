package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Booking;
import com.example.rentalcarsystem.model.Car;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


    List<Booking> findByCarId(Integer carId);
    boolean existsByCarId(Integer carId);

    Booking findBookingById(Integer id);

    List<Booking> findBookingByCustomer_Id(Integer customerId);

    Car findBookingByCarId(Integer carId);

    boolean existsByCarIdAndStartDateTimeAndEndDateTimeAndStatusNot(Integer carId, Instant startDateTime, Instant endDateTime, @Size(max = 50) String status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.car.id = :carId " +
            "AND b.status IN ('Confirmed', 'In process', 'Pending deposit') " +
            "AND (" +
            "(:startDateTime BETWEEN b.startDateTime AND b.endDateTime) OR " +
            "(:endDateTime BETWEEN b.startDateTime AND b.endDateTime) OR " +
            "(b.startDateTime BETWEEN :startDateTime AND :endDateTime)" +
            ")")
    boolean isCarAlreadyBooked(@Param("carId") Integer carId,
                               @Param("startDateTime") Instant startDateTime,
                               @Param("endDateTime") Instant endDateTime);

    List<Booking> findAllByCarIdAndStatus(Integer id, String s);

    List<Booking> findAllByCarIdAndStatusIn(Integer carId, Collection<String> statuses);
}
