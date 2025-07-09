package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
Car  creareNewCar (CarRequestDTO carRequestDTO);
List<Car> getAllCars();
Optional<Car> getCarById(int id);
List<Car> getCarByOwnerId(int id);
}
