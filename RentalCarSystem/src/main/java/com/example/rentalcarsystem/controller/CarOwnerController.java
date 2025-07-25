package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;

import com.example.rentalcarsystem.dto.response.other.PagingResponse;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.service.booking.BookingService;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import com.example.rentalcarsystem.service.feedback.FeedBackServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/car-rental/car-owner")
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
    public ResponseEntity<CarResponseDTO> createNewCar(@RequestBody CarRequestDTO carRequestDTO, HttpServletRequest request) {
        CarResponseDTO car = carService.createNewCar(carRequestDTO, request);
        return ResponseEntity.ok(car);
    }


    /**
     * Owner can view their car
     *
     * @param request
     * @return
     */
    @GetMapping("/car/my-car")
    public ResponseEntity<?> getCarByOwnerId(HttpServletRequest request,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "5") Integer size,
                                             @RequestParam (required = false) String name,
                                             @RequestParam (required = false)BigDecimal price,
                                             @RequestParam (required = false) String status) {
        Page<CarResponseDTO> listCar = carService.getOwnerCar(request, name, price, status, page, size);
        return ResponseEntity.ok(new PagingResponse<>(listCar));
    }


    /**
     * Update Car by owner
     *
     * @param carRequestDTO
     * @param carId         id of update Car
     * @return
     */
    @PutMapping("/car/my-car/{carId}/update-details-car")
    public ResponseEntity<CarDetailResponseDTO> updateCarDetails(@RequestBody CarRequestDTO carRequestDTO,
                                                                 @PathVariable Integer carId,
                                                                 HttpServletRequest request) {
        CarDetailResponseDTO updateCar = carService.updateCarDetails(carRequestDTO, carId, request);
        return ResponseEntity.ok(updateCar);
    }

    /**
     * Method allows car owner can stop renting the car
     *
     * @param carId   id of the car what car owner want to stop rent
     * @param request
     * @return
     */
    @PutMapping("/car/my-car/{carId}/stop-reting")
    public ResponseEntity<?> stopRentalCar(@PathVariable int carId, HttpServletRequest request) {
        return ResponseEntity.ok(carService.stopRentalCar(carId, request));
    }


    /**
     * Get All Feed Back Of Car Owner
     *
     * @param request
     * @return
     */
    @GetMapping("/feedback/feedback-report/{carId}")
    public ResponseEntity<FeedBackReportDTO> getAllFeedBackReport(HttpServletRequest request,
                                                                  @PathVariable Integer carId) {
        FeedBackReportDTO listFeedBackReport = feedBackService.getFeedBackReportByCarId(request, carId);
        return ResponseEntity.ok(listFeedBackReport);
    }

    /**
     * Confirm deposit
     *
     * @param request
     * @param bookingId
     * @return
     */
    @PutMapping("/booking/{bookingId}/confirm-deposit")
    public ResponseEntity<String> confirmDeposit(HttpServletRequest request, @PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.confirmDeposit(request, bookingId));
    }

    /**
     * Confirm payment
     *
     * @param request
     * @param bookingId
     * @return
     */
    @PutMapping("/booking/{bookingId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(HttpServletRequest request, @PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.confirmPayment(request, bookingId));
    }
}
