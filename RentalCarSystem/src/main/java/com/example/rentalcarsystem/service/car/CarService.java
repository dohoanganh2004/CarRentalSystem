package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.request.car.SearchRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.other.ListResultResponseDTO;
import com.example.rentalcarsystem.model.Car;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface CarService {
CarResponseDTO createNewCar (CarRequestDTO carRequestDTO, HttpServletRequest request);

CarDetailResponseDTO getCarById(int id);
Page<CarResponseDTO> getOwnerCar(HttpServletRequest request ,
                                 String name,
                                 BigDecimal price,
                                 String status,
                                 Integer page,Integer size);
ListResultResponseDTO<CarResponseDTO> searchCar(SearchRequestDTO searchRequestDTO);
CarDetailResponseDTO updateCarDetails(CarRequestDTO carRequestDTO,Integer carId,HttpServletRequest request);
String stopRentalCar(int carId,HttpServletRequest request);
}
