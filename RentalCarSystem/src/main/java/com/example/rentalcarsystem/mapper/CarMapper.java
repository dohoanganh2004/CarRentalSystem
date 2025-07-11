package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.model.Carimage;
import com.example.rentalcarsystem.model.Carowner;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {


    public static CarResponseDTO toDTO(Car car) {
        CarResponseDTO dto = new CarResponseDTO();
        dto.setName(car.getName());
        dto.setPrice(car.getBasePrice());
        dto.setStatus(car.getStatus());
        if (car.getCarImage() != null) {
            dto.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
            dto.setBackImageUrl(car.getCarImage().getBackImageUrl());
            dto.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
            dto.setRightImageUrl(car.getCarImage().getRightImageUrl());
        }
        dto.setNoOfRides(car.getMileage());
        dto.setPrice(car.getBasePrice());
        dto.setLocation(car.getAddress());

        return dto;
    }

    public static CarDetailResponseDTO toDetailDTO(Car car) {
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

    public static Car fromDTO(CarRequestDTO dto, Carowner owner) {
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
        car.setDeposit(dto.getDeposit());
        car.setDescription(dto.getDescription());
        car.setAdditionalFunctions(dto.getAdditionalFunctions());
        car.setTermsOfUse(dto.getTermsOfUse());
        car.setStatus("Available");
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
        String  carName = dto.getBrandName().concat(" ").concat(dto.getModel().concat(" ").concat(dto.getProductionYear().toString()));
        car.setName(carName);
        return car;
    }
}
