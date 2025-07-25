package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.response.booking.CarInformationDTO;
import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.model.*;
import com.example.rentalcarsystem.repository.BookingRepository;
import com.example.rentalcarsystem.repository.FeedBackRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarMapper {
    private final BookingRepository bookingRepository;
    private final FeedBackRepository feedBackRepository;

    public CarMapper(BookingRepository bookingRepository, FeedBackRepository feedBackRepository) {
        this.bookingRepository = bookingRepository;
        this.feedBackRepository = feedBackRepository;
    }

    /**
     * Convert information from Car to CarResponseDTO
     * @param car obj car
     * @return dto
     */
    public CarResponseDTO toDTO(Car car) {

        Double carRating = findRatingOfCar(car.getId());
        CarResponseDTO dto = new CarResponseDTO();
        dto.setName(car.getName());
        dto.setRating(carRating);
        dto.setPrice(car.getBasePrice());

        if (car.getCarImage() != null) {
            dto.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
            dto.setBackImageUrl(car.getCarImage().getBackImageUrl());
            dto.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
            dto.setRightImageUrl(car.getCarImage().getRightImageUrl());
        }
        dto.setNoOfRides(car.getMileage());
        dto.setPrice(car.getBasePrice());
        dto.setLocation(car.getAddress());
        dto.setStatus(getDynamicCarStatus(car));
        return dto;
    }


    /**
     * Method to convert from car to CarDetailResponseDTO
     *
     * @param car object car
     * @return dto
     */
    public CarDetailResponseDTO toDetailDTO(Car car) {
        CarDetailResponseDTO dto = new CarDetailResponseDTO();
        dto.setLicensePlate(car.getLicensePlate());
        dto.setColor(car.getColor());
        dto.setBrandName(car.getBrand());
        dto.setModel(car.getModel());
        dto.setProductionYear(car.getProductionYears());
        dto.setNoOfSeats(car.getNumberOfSeats());
        dto.setTransmission(car.getTransmissionType());
        dto.setFuel(car.getFuelType());
        dto.setMileage(car.getMileage());
        dto.setFuelConsumption(car.getFuelConsumption());
        dto.setAddress(car.getAddress());
        dto.setDescription(car.getDescription());
        dto.setAdditionalFunctions(car.getAdditionalFunctions());
        dto.setBasePrice(car.getBasePrice());
        dto.setDeposit(car.getDeposit());
        dto.setTermsOfUse(car.getTermsOfUse());
        dto.setRegistrationPaperUrl(car.getRegistrationPaperUrl());
        dto.setCertificateOfInspectionUrl(car.getCertificateOfInspectionUrl());
        dto.setInsuranceUrl(car.getInsuranceUrl());
        if (car.getCarImage() != null) {
            dto.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
            dto.setBackImageUrl(car.getCarImage().getBackImageUrl());
            dto.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
            dto.setRightImageUrl(car.getCarImage().getRightImageUrl());
        }
        return dto;
    }

    /**
     * Method to convert from car to carInformationDTO
     *
     * @param car object car
     * @return dto
     */
    public CarInformationDTO toInformationDTO(Car car) {
        CarInformationDTO dto = new CarInformationDTO();
        dto.setLicensePlate(car.getLicensePlate());
        dto.setColor(car.getColor());
        dto.setBrandName(car.getBrand());
        dto.setModel(car.getModel());
        dto.setProductionYear(car.getProductionYears());
        dto.setNoOfSeats(car.getNumberOfSeats());
        dto.setTransmission(car.getTransmissionType());
        dto.setFuel(car.getFuelType());
        dto.setMileage(car.getMileage());
        dto.setFuelConsumption(car.getFuelConsumption());
        dto.setAddress(car.getAddress());
        dto.setDescription(car.getDescription());
        dto.setAdditionalFunctions(car.getAdditionalFunctions());
        dto.setBasePrice(car.getBasePrice());
        dto.setTermsOfUse(car.getTermsOfUse());
        dto.setRegistrationPaperUrl(car.getRegistrationPaperUrl());
        dto.setCertificateOfInspectionUrl(car.getCertificateOfInspectionUrl());
        dto.setInsuranceUrl(car.getInsuranceUrl());

        return dto;
    }


    /**
     * Method convert from dto to Car
     *
     * @param dto
     * @param owner
     * @return
     */
    public Car fromDTO(CarRequestDTO dto, Carowner owner) {
        Car car = new Car();
        car.setCarOwner(owner);
        car.setLicensePlate(dto.getLicensePlate());
        car.setBrand(dto.getBrandName());
        car.setModel(dto.getModel());
        car.setColor(dto.getColor());
        car.setNumberOfSeats(dto.getNoOfSeats());
        car.setProductionYears(dto.getProductionYear());
        car.setTransmissionType(dto.getTransmission());
        car.setFuelType(dto.getFuel());
        car.setMileage(dto.getMileage());
        car.setFuelConsumption(dto.getFuelConsumption());
        car.setBasePrice(dto.getBasePrice());
        car.setDeposit(dto.getBasePrice().multiply(new BigDecimal("0.2")).add(dto.getBasePrice()));
        car.setDescription(dto.getDescription());
        car.setAdditionalFunctions(dto.getAdditionalFunctions());
        car.setTermsOfUse(dto.getTermsOfUse());

        car.setRegistrationPaperUrl(dto.getRegistrationPaperUrl());
        car.setCertificateOfInspectionUrl(dto.getCertificateOfInspectionUrl());
        car.setInsuranceUrl(dto.getInsuranceUrl());

        String address = String.join(" - ",
                dto.getHouseNumberOrStreet(),
                dto.getWard(),
                dto.getDistrict(),
                dto.getCityOrProvince());
        car.setAddress(address);

        Carimage image = new Carimage();
        image.setFrontImageUrl(dto.getFrontImageUrl());
        image.setBackImageUrl(dto.getBackImageUrl());
        image.setLeftImageUrl(dto.getLeftImageUrl());
        image.setRightImageUrl(dto.getRightImageUrl());
        image.setCar(car);
        car.setCarImage(image);
        String carName = dto.getBrandName().concat(" ").concat(dto.getModel().concat(" ").concat(dto.getProductionYear().toString()));
        car.setName(carName);
        car.setIsActive(dto.getIsActive());
        return car;
    }

    /**
     * Method to get  rating of car
     *
     * @param carId id of car want to get rating
     * @return avg rating of this car
     */
    public Double findRatingOfCar(Integer carId) {
        List<Booking> listBooking = bookingRepository.findByCarId(carId);
        double sumRating = 0;
        int totalFeedbackCount = 0;
        double averageRating = 0.0;
        double rating = 0;

        for (Booking booking : listBooking) {
            List<Feedback> feedbacks = feedBackRepository.findByBookingId(booking.getId());

            for (Feedback feedback : feedbacks) {
                System.out.println("Rating: " + feedback.getRatings());
                sumRating += feedback.getRatings();
                totalFeedbackCount++;
            }
        }

        if (totalFeedbackCount == 0) {
            rating = 0;

        }
        averageRating = sumRating / totalFeedbackCount;
        rating = Math.ceil(averageRating * 2) / 2;
        return rating;
    }

    /**
     * Method to change status of car
     *
     * @param car obj car
     * @return status of car
     */
    public String getDynamicCarStatus(Car car) {
        // xe bi dung cho muon
        if (!car.getIsActive()) {
            return "Stopped";
        }

        Instant now = Instant.now();
        List<String> statusList = List.of("Pending Deposit", "Confirmed", "In Progress", "Pending Payment");
        List<Booking> bookings = bookingRepository.findAllByCarIdAndStatusIn( car.getId(), statusList);
        for (Booking booking : bookings) {
                return "Booked";

        }

        return "Available";
    }

}



