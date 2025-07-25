package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.SearchRequestDTO;
import com.example.rentalcarsystem.dto.response.booking.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.response.booking.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.request.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.response.booking.BookingResultDTO;
import com.example.rentalcarsystem.dto.request.feedback.FeedBackRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.feedback.PublicCarFeedBackDTO;
import com.example.rentalcarsystem.dto.response.other.ListResultResponseDTO;
import com.example.rentalcarsystem.dto.response.rental.MyRentalResponseDTO;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import com.example.rentalcarsystem.service.feedback.FeedBackServiceImpl;
import com.example.rentalcarsystem.service.booking.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/car-rental/customer")
public class CustomerController {
    private final CarServiceImpl carService;
    private final FeedBackServiceImpl feedBackService;
    private final BookingServiceImpl bookingService;

    public CustomerController(CarServiceImpl carService, FeedBackServiceImpl feedBackService, BookingServiceImpl bookingService) {
        this.carService = carService;
        this.feedBackService = feedBackService;
        this.bookingService = bookingService;
    }

    /**
     *
     * Search available car
     * @param searchRequestDTO
     * @return
     */
    @GetMapping("/search-car")
    public ResponseEntity<ListResultResponseDTO<CarResponseDTO>> searchAvailableCar(@RequestBody SearchRequestDTO searchRequestDTO) {

        ListResultResponseDTO<CarResponseDTO> listAvailableCar = carService.searchCar(searchRequestDTO);
        return ResponseEntity.ok(listAvailableCar);

    }

    /**
     * View Detail Of Car With ID
     *
     * @param id
     * @return
     */
    @GetMapping("car/{id}/car-details")
    public ResponseEntity<CarDetailResponseDTO> viewCarDetail(@PathVariable int id) {
        CarDetailResponseDTO carDetail = carService.getCarById(id);
        return ResponseEntity.ok(carDetail);
    }

    /**
     * Get list feed back of car
     *
     * @param carId
     * @return
     */
    @GetMapping("/car/{id}/feedback")
    public ResponseEntity<List<PublicCarFeedBackDTO>> viewPublicCarFeedBack(@RequestBody int carId) {
        return ResponseEntity.ok(feedBackService.getFeedBackByCarId(carId));

    }

    /**
     * Customer can booking the car
     *
     * @param bookingInformationDTO
     * @return
     */
    @PostMapping("/booking/new-booking")
    public ResponseEntity<BookingResultDTO> bookingCar(@RequestBody BookingInformationDTO bookingInformationDTO,
                                                       HttpServletRequest request) {
        return ResponseEntity.ok(bookingService.rentalCar(bookingInformationDTO, request));
    }

    /**
     * Get my booking
     *
     * @param request
     * @return
     */
    @GetMapping("/booking/my-booking")
    public ResponseEntity<MyRentalResponseDTO> viewMyBookingList(HttpServletRequest request) {
        MyRentalResponseDTO myRentalResponseDTO = bookingService.listBookings(request);
        return ResponseEntity.ok(myRentalResponseDTO);
    }

    /**
     * Customer can view booking detail
     *
     * @param id
     * @return
     */
    @GetMapping("/booking/my-booking/{id}/booking-detail")
    public ResponseEntity<CarBookingDetailsDTO> viewBookingDetails(@PathVariable int id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    /**
     * Customer can cancel their booking and
     *
     * @param bookingId
     * @param request
     * @return
     */
    @PutMapping("/booking/my-booking/{bookingId}/cancel-booking")
    public ResponseEntity<CarBookingBaseInfoDTO> cancelBooking(@PathVariable int bookingId, HttpServletRequest request) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, request));
    }

    /**
     * This use case allow customers to confirm picking up a car and change booking status
     *
     * @param bookingId
     * @param request
     * @return
     */
    @PutMapping("/booking/my-booking/{bookingId}/pick-up")
    public ResponseEntity<CarBookingBaseInfoDTO> pickUpCarBooking(@PathVariable int bookingId, HttpServletRequest request) {
        return ResponseEntity.ok(bookingService.pickUpBooking(bookingId, request));
    }

    /**
     *
     * @param bookingId
     * @param request
     * @return
     */
    @PutMapping("/booking/my-booking/{bookingId}/return-car")
    public ResponseEntity<BookingResultDTO> returnTheCar( @PathVariable Integer bookingId, HttpServletRequest request) {
        return ResponseEntity.ok(bookingService.returnTheCar(bookingId, request));
    }

    /**
     * Create feedback after return the car
     * @param feedBackRequestDTO
     * @return
     */
    @PostMapping("/booking/my-booking/{bookingId}/feedback")
    public ResponseEntity<?> feedBackBooking(@RequestBody FeedBackRequestDTO feedBackRequestDTO) {
        return ResponseEntity.ok(feedBackService.createFeedBack(feedBackRequestDTO));
    }

}
