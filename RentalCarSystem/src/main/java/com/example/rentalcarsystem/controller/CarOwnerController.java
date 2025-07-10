package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.service.car.CarService;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car-owner")
public class CarOwnerController {
private final CarServiceImpl carService;
private final JwtTokenProvider jwtTokenProvider;

public CarOwnerController(CarServiceImpl carService, JwtTokenProvider jwtTokenProvider) {
    this.carService = carService;
    this.jwtTokenProvider = jwtTokenProvider;
}

    /**
     * Add car for rental
     * @param carRequestDTO
     * @return
     */
    @PostMapping("/add-car")
    public ResponseEntity<CarResponseDTO> creareNewCar (@RequestBody CarRequestDTO carRequestDTO, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        Car car = carService.creareNewCar(carRequestDTO,userId);
        return ResponseEntity.ok(new CarResponseDTO());
    }


    /**
     * Owner can view their car
     * @param request
     * @return
     */
    @GetMapping("/my-car")
    public List<CarResponseDTO> getCarByOwnerId(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int ownerId = jwtTokenProvider.getUserIdFromToken(token);
        return carService.getCarByOwnerId(ownerId);
    }

    /**
     * Update Car by owner
     * @param carRequestDTO
     * @param carId
     * @return
     */
    @PutMapping("/my-car/update-details-car/{carId}")
    public ResponseEntity<CarResponseDTO> updateCarDetails(@RequestBody CarRequestDTO carRequestDTO, @PathVariable Integer carId) {
        carService.updateCarDetails(carRequestDTO,carId);
        return ResponseEntity.ok(new CarResponseDTO());
    }
    /**
     * Get Token from request
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
