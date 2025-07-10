package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CarServiceImpl carService;

    public CustomerController(CarServiceImpl carService) {
        this.carService = carService;
    }

    /**
     * Search a car from customer
     * @param location
     * @param pickupDateTime
     * @param dropOffDateTime
     * @param page
     * @param size
     * @param sortBy
     * @param order
     * @return
     */
    @GetMapping("/search-car")
    public Page<CarResponseDTO> searchCars(String location
            , LocalDateTime pickupDateTime
            , LocalDateTime dropOffDateTime
            , Integer page
            , Integer size
            , String sortBy, String order) {
        Page<CarResponseDTO> availableCars = carService.getAvailableCars(location, pickupDateTime, dropOffDateTime, page, size, sortBy, order);
        return availableCars;

    }

    /**
     * View Detail Of Car With ID
     * @param id
     * @return
     */
    @GetMapping("/car-details/{id}")
    public ResponseEntity<CarDetailResponseDTO> viewCarDetail(@PathVariable int id) {
        CarDetailResponseDTO carDetail = carService.getCarById(id);
       return ResponseEntity.ok(carDetail);
    }
}
