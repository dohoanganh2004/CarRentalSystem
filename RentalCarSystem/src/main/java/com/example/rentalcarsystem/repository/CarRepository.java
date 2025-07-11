package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Car;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
    List<Car> getAllByCarOwner_Id(Integer carOwnerId);

    boolean existsByLicensePlate(@Size(max = 20) String licensePlate);

    @Query("""
SELECT c FROM Car c
WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :location, '%'))
AND c.id NOT IN (
    SELECT b.car.id FROM Booking b
    WHERE b.status = 'Booked'
    AND (
        (:startDateTime BETWEEN b.startDateTime AND b.endDateTime)
        OR (:endDateTime BETWEEN b.startDateTime AND b.endDateTime)
        OR (b.startDateTime BETWEEN :startDateTime AND :endDateTime)
    )
)
""")
    List<Car> searchAvailableCars(
            @Param("location") String location,
            @Param("startDateTime") Instant startDateTime,
            @Param("endDateTime") Instant endDateTime
    );

}
