package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarService {
Car  creareNewCar (CarRequestDTO carRequestDTO,Integer carOwnerId);
List<Car> getAllCars();
CarDetailResponseDTO getCarById(int id);
List<Car> getCarByOwnerId(int id);
Page<CarResponseDTO> getAvailableCars(String location,
                                      LocalDateTime pickupDateTime,
                                      LocalDateTime dropOffDateTime,
                                      Integer page,
                                      Integer size,
                                      String sortBy,
                                      String order);
}
