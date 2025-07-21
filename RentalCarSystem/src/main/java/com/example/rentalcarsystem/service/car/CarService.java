package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.other.ListResultResponseDTO;
import com.example.rentalcarsystem.model.Car;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

public interface CarService {
CarResponseDTO creareNewCar (CarRequestDTO carRequestDTO, HttpServletRequest request);

CarDetailResponseDTO getCarById(int id);
Page<CarResponseDTO> getOwnerCar(HttpServletRequest request ,Integer page,Integer size);
ListResultResponseDTO<CarResponseDTO> searchCar(String location, Instant startDateTime, Instant endDateTime);
CarDetailResponseDTO updateCarDetails(CarRequestDTO carRequestDTO,Integer carId,HttpServletRequest request);
CarResponseDTO stopRentalCar(int carId,HttpServletRequest request);
}
