package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
    List<Car> getAllByCarOwner_Id(Integer carOwnerId);
}
