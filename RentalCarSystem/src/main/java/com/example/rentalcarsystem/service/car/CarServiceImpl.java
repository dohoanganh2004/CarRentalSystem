package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.mapper.CarMapper;
import com.example.rentalcarsystem.model.*;
import com.example.rentalcarsystem.repository.*;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.untils.FileStoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final FeedBackRepository feedBackRepository;
    private final CarImageRepository carImageRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CarMapper carMapper;
    private final FileStoreService fileStoreService;

    public CarServiceImpl(CarRepository carRepository,
                          CarOwnerRepository carOwnerRepository,
                          UserRepository userRepository,
                          BookingRepository bookingRepository,
                          FeedBackRepository feedBackRepository,
                           CarImageRepository carImageRepository,
                          JwtTokenProvider jwtTokenProvider,
                          CarMapper carMapper,
                          FileStoreService fileStoreService) {
        this.carRepository = carRepository;
        this.carOwnerRepository = carOwnerRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.feedBackRepository = feedBackRepository;
        this.carImageRepository= carImageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.carMapper = carMapper;
        this.fileStoreService = fileStoreService;
    }

    /**
     * Method to create new Car
     *
     * @param carRequestDTO
     * @return
     */
    @Override
    public CarResponseDTO creareNewCar(CarRequestDTO carRequestDTO, HttpServletRequest request) {
          String token = getTokenFromRequest(request);
          int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);

          Carowner carowner = carOwnerRepository.findById(carOwnerId).orElseThrow(() -> new RuntimeException("Car owner not found"));

          Car newCar = carMapper.fromDTO(carRequestDTO, carowner);
        if (carRequestDTO.getFrontImage() != null && !carRequestDTO.getFrontImage().isEmpty()) {
            String frontImageUrl = fileStoreService.saveFile(carRequestDTO.getFrontImage(), "uploads", "frontImage");
            carRequestDTO.setFrontImageUrl(frontImageUrl);
        }

        if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
            String backImageUrl =fileStoreService.saveFile(carRequestDTO.getBackImage(), "uploads", "backImage");
            carRequestDTO.setBackImageUrl(backImageUrl);
        }

        if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
            String rightImageUrl = fileStoreService.saveFile(carRequestDTO.getRightImage(), "uploads", "rightImage");
            carRequestDTO.setRightImageUrl(rightImageUrl);
        }

        if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
            String leftImageUrl = fileStoreService.saveFile(carRequestDTO.getLeftImage(), "uploads", "leftImage");
            carRequestDTO.setLeftImageUrl(leftImageUrl);
        }
        if (carRequestDTO.getRegistrationPaper() != null && !carRequestDTO.getRegistrationPaper().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getRegistrationPaper(), "uploads", "registration_paper");
            carRequestDTO.setRegistrationPaperUrl(url);
        }

        if (carRequestDTO.getCertificateOfInspection() != null && !carRequestDTO.getCertificateOfInspection().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getCertificateOfInspection(), "uploads", "inspection_certificate");
            carRequestDTO.setCertificateOfInspectionUrl(url);
        }

        if (carRequestDTO.getInsurance() != null && !carRequestDTO.getInsurance().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getInsurance(), "uploads", "insurance");
            carRequestDTO.setInsuranceUrl(url);
        }
        carRepository.save(newCar);
        log.info("New car created");
        return carMapper.toDTO(newCar);
    }








    /**
     * Get Car By ID
     * @param id
     * @return
     */
    @Override
    public CarDetailResponseDTO getCarById(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car Not Found"));
        CarDetailResponseDTO carDetailResponseDTO = carMapper.toDetailDTO(car);
        return carDetailResponseDTO ;
    }


    /**
     * Get List Car of Owner
     * @param request
     * @return
     */
    @Override
    public List<CarResponseDTO> getOwnerCar(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Integer carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        List<Car> listCarOfOwner = carRepository.getAllByCarOwner_Id(carOwnerId);
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();
        for (Car car : listCarOfOwner) {
            CarResponseDTO carResponseDTO = carMapper.toDTO(car);
            carResponseDTOList.add(carResponseDTO);
        }
        return carResponseDTOList;
    }

    /**
     * Search Available Car
     * @param location
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public List<CarResponseDTO> searchCar(String location, Instant startDateTime, Instant endDateTime) {
        List<Car> listAvailableCar = carRepository.searchAvailableCars(location,startDateTime,endDateTime);
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();
        for (Car car : listAvailableCar) {
            CarResponseDTO carResponseDTO = carMapper.toDTO(car);
            carResponseDTOList.add(carResponseDTO);
        }
        return carResponseDTOList;
    }

    @Transactional
    public CarDetailResponseDTO updateCarDetails(CarRequestDTO carRequestDTO, Integer carId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);


        Carowner currentCarOwner = carOwnerRepository.findById(carOwnerId)
                .orElseThrow(() -> new RuntimeException("Car owner not found"));


        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car with id " + carId + " not found"));


        if (!car.getCarOwner().getId().equals(currentCarOwner.getId())) {
            throw new RuntimeException("You are not allowed to update this car");
        }


        car.setLicensePlate(carRequestDTO.getLicensePlate());
        car.setColor(carRequestDTO.getColor());
        car.setBrand(carRequestDTO.getBrandName());
        car.setModel(carRequestDTO.getModel());
        car.setProductionYears(carRequestDTO.getProductionYear());
        car.setNumberOfSeats(carRequestDTO.getNoOfSeats());
        car.setTransmissionType(carRequestDTO.getTransmission());
        car.setFuelType(carRequestDTO.getFuel());
        car.setMileage(carRequestDTO.getMileage());
        car.setFuelConsumption(carRequestDTO.getFuelConsumption());
        car.setDescription(carRequestDTO.getDescription());
        car.setAdditionalFunctions(carRequestDTO.getAdditionalFunctions());
        car.setBasePrice(carRequestDTO.getBasePrice());
        car.setDeposit(carRequestDTO.getDeposit());
        car.setTermsOfUse(carRequestDTO.getTermsOfUse());

        String fullAddress = String.join(" - ",
                carRequestDTO.getHouseNumberOrStreet(),
                carRequestDTO.getWard(),
                carRequestDTO.getDistrict(),
                carRequestDTO.getCityOrProvince());
        car.setAddress(fullAddress);
        car.setStatus(carRequestDTO.getStatus());


        if (carRequestDTO.getRegistrationPaper() != null && !carRequestDTO.getRegistrationPaper().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getRegistrationPaper(), "uploads", "documents/registration");
            car.setRegistrationPaperUrl(url);
        }

        if (carRequestDTO.getCertificateOfInspection() != null && !carRequestDTO.getCertificateOfInspection().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getCertificateOfInspection(), "uploads", "documents/inspection");
            car.setCertificateOfInspectionUrl(url);
        }

        if (carRequestDTO.getInsurance() != null && !carRequestDTO.getInsurance().isEmpty()) {
            String url = fileStoreService.saveFile(carRequestDTO.getInsurance(), "uploads", "documents/insurance");
            car.setInsuranceUrl(url);
        }


        Carimage carImage = car.getCarImage();
        if (carImage != null) {
            if (carRequestDTO.getFrontImage() != null && !carRequestDTO.getFrontImage().isEmpty()) {
                carImage.setFrontImageUrl(fileStoreService.saveFile(carRequestDTO.getFrontImage(), "uploads", "image/frontImage"));
            }
            if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
                carImage.setBackImageUrl(fileStoreService.saveFile(carRequestDTO.getBackImage(), "uploads", "image/backImage"));
            }
            if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
                carImage.setLeftImageUrl(fileStoreService.saveFile(carRequestDTO.getLeftImage(), "uploads", "image/leftImage"));
            }
            if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
                carImage.setRightImageUrl(fileStoreService.saveFile(carRequestDTO.getRightImage(), "uploads", "image/rightImage"));
            }
            carImageRepository.save(carImage);
        }

        Car updatedCar = carRepository.save(car);
        return carMapper.toDetailDTO(updatedCar);
    }





    /**
     * Method to get rating of car
     *
     * @param carId
     * @return
     */
    public Integer findRatingOfCar(Integer carId) {
        List<Booking> listBooking = bookingRepository.findByCarId(carId);
        int sumRating = 0;
        int totalFeedbackCount = 0;

        for (Booking booking : listBooking) {
            List<Feedback> feedbacks = feedBackRepository.findByBookingId(booking.getId());

            for (Feedback feedback : feedbacks) {
                System.out.println("Rating: " + feedback.getRatings());
                sumRating += feedback.getRatings();
                totalFeedbackCount++;
            }
        }

        if (totalFeedbackCount == 0) {
            return 0;
        }

        return sumRating / totalFeedbackCount;
    }


    /**
     * Kiem ta xem xe da duoc muon hay chua
     *
     * @param carId
     * @return
     */
    public String addStatus(Integer carId, LocalDateTime pickupDateTime, LocalDateTime dropOffDateTime) {
      String status = "Available";

      if(pickupDateTime != null && dropOffDateTime != null) {
         List<Booking> listBooking = bookingRepository.findByCarId(carId);
         for (Booking booking : listBooking) {
             LocalDateTime bookingStart = LocalDateTime.ofInstant(booking.getStartDateTime(), ZoneId.systemDefault());
             LocalDateTime bookingEnd = LocalDateTime.ofInstant(booking.getEndDateTime(), ZoneId.systemDefault());
             if(!(pickupDateTime.isBefore(bookingStart) && dropOffDateTime.isAfter(bookingEnd)) ) {
             status = "Not Available";
             break;
             }
         }
      }
        return status;
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
