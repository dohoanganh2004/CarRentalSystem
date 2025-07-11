package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;

import com.example.rentalcarsystem.service.car.CarServiceImpl;
import com.example.rentalcarsystem.service.feedback.FeedBackServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car-owner")
public class CarOwnerController {
    private final CarServiceImpl carService;

    private final FeedBackServiceImpl feedBackService;

    public CarOwnerController(CarServiceImpl carService, FeedBackServiceImpl feedBackService) {
        this.carService = carService;
        this.feedBackService = feedBackService;
    }

    /**
     * Add car for rental
     *
     * @param carRequestDTO
     * @return
     */
    @PostMapping("/add-car")
    public ResponseEntity<CarResponseDTO> creareNewCar(@RequestBody CarRequestDTO carRequestDTO, HttpServletRequest request) {
        CarResponseDTO car = carService.creareNewCar(carRequestDTO, request);
        return ResponseEntity.ok(car);
    }


    /**
     * Owner can view their car
     *
     * @param request
     * @return
     */
    @GetMapping("/my-car")
    public List<CarResponseDTO> getCarByOwnerId(HttpServletRequest request) {
        return carService.getOwnerCar(request);
    }


    /**
     * Update Car by owner
     *
     * @param carRequestDTO
     * @param carId
     * @return
     */
    @PutMapping("/my-car/update-details-car/{carId}")
    public ResponseEntity<CarDetailResponseDTO> updateCarDetails(@RequestBody CarRequestDTO carRequestDTO,
                                                                 @PathVariable Integer carId,
                                                                 HttpServletRequest request) {
        CarDetailResponseDTO updateCar = carService.updateCarDetails(carRequestDTO, carId, request);
        return ResponseEntity.ok(updateCar);
    }

    /**
     * Get All Feed Back Of Car Owner
     *
     * @param request
     * @return
     */
    @GetMapping("/feedback-report")
    public ResponseEntity<List<FeedBackReportDTO>> getAllFeedBackReport(HttpServletRequest request) {
        List<FeedBackReportDTO> listFeedBackReport = feedBackService.getFeedBackReportByCaOwnerId(request);
        return ResponseEntity.ok(listFeedBackReport);
    }

}
