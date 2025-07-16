package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;

import com.example.rentalcarsystem.service.booking.BookingService;
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
    private final BookingService bookingService;

    public CarOwnerController(CarServiceImpl carService, FeedBackServiceImpl feedBackService, BookingService bookingService) {
        this.carService = carService;
        this.feedBackService = feedBackService;
        this.bookingService = bookingService;
    }

    /**
     * Add car for rental
     *
     * @param carRequestDTO
     * @return
     */
    @PostMapping("/car/add-car")
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
    @GetMapping("/car/my-car")
    public List<CarResponseDTO> getCarByOwnerId(HttpServletRequest request) {
        return carService.getOwnerCar(request);
    }


    /**
     * Update Car by owner
     *
     * @param carRequestDTO
     * @param carId id of update Car
     * @return
     */
    @PutMapping("/car/my-car/update-details-car/{carId}")
    public ResponseEntity<CarDetailResponseDTO> updateCarDetails(@RequestBody CarRequestDTO carRequestDTO,
                                                                 @PathVariable Integer carId,
                                                                 HttpServletRequest request) {
        CarDetailResponseDTO updateCar = carService.updateCarDetails(carRequestDTO, carId, request);
        return ResponseEntity.ok(updateCar);
    }

    /**
     * Method allows car owner can stop renting the car
     * @param carId
     * @param request
     * @return
     */
    @PutMapping("/car/my-car/stop-reting/{carId}")
    public ResponseEntity<CarResponseDTO> stopRentalCar(@PathVariable int carId, HttpServletRequest request) {
        return ResponseEntity.ok(carService.stopRentalCar(carId, request));
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

    /**
     * Confirm deposit
     * @param request
     * @param bookingId
     * @return
     */
    @PutMapping("/booking/{bookingId}/confirm-deposit")
    public ResponseEntity<String> confirmDeposit(HttpServletRequest request,@PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.confirmDeposit(request,bookingId));
    }

    /**
     * Confirm payment
     * @param request
     * @param bookingId
     * @return
     */
    @PutMapping("/booking/{bookingId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(HttpServletRequest request,@PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.confirmPayment(request,bookingId));
    }
}
