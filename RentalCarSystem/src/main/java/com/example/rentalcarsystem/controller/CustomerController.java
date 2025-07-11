package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CarServiceImpl carService;

    public CustomerController(CarServiceImpl carService) {
        this.carService = carService;
    }
@GetMapping("/search-car")
public ResponseEntity<List<CarResponseDTO>> searchAvailableCar(@RequestParam String location,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
    Instant start = startDateTime.atZone(ZoneId.systemDefault()).toInstant();
    Instant end = endDateTime.atZone(ZoneId.systemDefault()).toInstant();
    List<CarResponseDTO> listAvailableCar = carService.searchCar(location,start,end);
    return ResponseEntity.ok(listAvailableCar);

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
