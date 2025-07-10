package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.model.*;
import com.example.rentalcarsystem.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final FeedBackRepository feedBackRepository;
    private final CarImageRepository carImageRepository;

    public CarServiceImpl(CarRepository carRepository,
                          CarOwnerRepository carOwnerRepository,
                          UserRepository userRepository,
                          BookingRepository bookingRepository,
                          FeedBackRepository feedBackRepository,
                           CarImageRepository carImageRepository) {
        this.carRepository = carRepository;
        this.carOwnerRepository = carOwnerRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.feedBackRepository = feedBackRepository;
        this.carImageRepository= carImageRepository;
    }

    /**
     * Method to create new Car
     *
     * @param carRequestDTO
     * @return
     */
    @Override
    public Car creareNewCar(CarRequestDTO carRequestDTO, Integer carOwnerId) {



        if (carRequestDTO.getFrontImage() != null && !carRequestDTO.getFrontImage().isEmpty()) {
            String frontImageUrl = saveFile(carRequestDTO.getFrontImage(), "uploads", "frontImage");
            carRequestDTO.setFrontImageUrl(frontImageUrl);
        }

        if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
            String backImageUrl = saveFile(carRequestDTO.getBackImage(), "uploads", "backImage");
            carRequestDTO.setBackImageUrl(backImageUrl);
        }

        if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
            String rightImageUrl = saveFile(carRequestDTO.getRightImage(), "uploads", "rightImage");
            carRequestDTO.setRightImageUrl(rightImageUrl);
        }

        if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
            String leftImageUrl = saveFile(carRequestDTO.getLeftImage(), "uploads", "leftImage");
            carRequestDTO.setLeftImageUrl(leftImageUrl);
        }

        Carowner carOwner = carOwnerRepository.findById(carOwnerId).orElseThrow(() -> new RuntimeException("Car Owner Not Found"));

        Car savedCar = fromDTOtoCar(carRequestDTO, carOwner);
        carRepository.save(savedCar);
        log.info("New car created");
        return savedCar;
    }

    /**
     * Get all car
     * @return
     */
    @Override
    public List<Car> getAllCars() {
        List<Car> carList = carRepository.findAll();
        return carList;
    }

    /**
     * Get Car By ID
     * @param id
     * @return
     */
    @Override
    public CarDetailResponseDTO getCarById(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car Not Found"));
        CarDetailResponseDTO carDetailResponseDTO = toCarDetailsResponseDTO(car);
        return carDetailResponseDTO ;
    }


    /**
     * Get Oner Car
     * @param ownerId
     * @return
     */
    @Override
    public List<CarResponseDTO> getCarByOwnerId(int ownerId) {
        List<Car> listCarOfOwner = carRepository.getAllByCarOwner_Id(ownerId);
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();
        for (Car car : listCarOfOwner) {
            CarResponseDTO carResponseDTO = toDTO(car);
            carResponseDTOList.add(carResponseDTO);
        }
        return carResponseDTOList;
    }

    @Transactional
    public CarDetailResponseDTO updateCarDetails(CarRequestDTO carRequestDTO, Integer carId) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car with id " + carId + " not found"));


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
            String url = saveFile(carRequestDTO.getRegistrationPaper(), "uploads", "documents/registration");
            car.setRegistrationPaperUrl(url);
        }

        if (carRequestDTO.getCertificateOfInspection() != null && !carRequestDTO.getCertificateOfInspection().isEmpty()) {
            String url = saveFile(carRequestDTO.getCertificateOfInspection(), "uploads", "documents/inspection");
            car.setCertificateOfInspectionUrl(url);
        }

        if (carRequestDTO.getInsurance() != null && !carRequestDTO.getInsurance().isEmpty()) {
            String url = saveFile(carRequestDTO.getInsurance(), "uploads", "documents/insurance");
            car.setInsuranceUrl(url);
        }


        Carimage carImage = car.getCarImage();
        if (carImage != null) {
            if (carRequestDTO.getFrontImage() != null && !carRequestDTO.getFrontImage().isEmpty()) {
                carImage.setFrontImageUrl(saveFile(carRequestDTO.getFrontImage(), "uploads", "image/frontImage"));
            }
            if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
                carImage.setBackImageUrl(saveFile(carRequestDTO.getBackImage(), "uploads", "image/backImage"));
            }
            if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
                carImage.setLeftImageUrl(saveFile(carRequestDTO.getLeftImage(), "uploads", "image/leftImage"));
            }
            if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
                carImage.setRightImageUrl(saveFile(carRequestDTO.getRightImage(), "uploads", "image/rightImage"));
            }
            carImageRepository.save(carImage);
        }


        Car updatedCar = carRepository.save(car);


        return toCarDetailsResponseDTO(updatedCar);
    }


//    /**
//     * Get all car available with serach
//     * @param location
//     * @param pickupDateTime
//     * @param dropOffDateTime
//     * @param page
//     * @param size
//     * @param sortBy
//     * @param order
//     * @return
//     */
//    @Override
//    public Page<CarResponseDTO> getAvailableCars(String location, LocalDateTime pickupDateTime,
//                                                 LocalDateTime dropOffDateTime,
//                                                 Integer page,
//                                                 Integer size, String sortBy, String order) {
//
//        return null;
//    }

    /**
     * Change information from CarRequestDTO to Car
     *
     * @param carRequestDTO
     * @return
     */

    public Car fromDTOtoCar(CarRequestDTO carRequestDTO, Carowner carOwner) {
        Car car = new Car();

        car.setCarOwner(carOwner);
        String carName = carRequestDTO.getBrandName().
                concat(" ").concat(carRequestDTO.getModel()).concat(" ").
                concat(carRequestDTO.getProductionYear().toString());

        car.setLicensePlate(carRequestDTO.getLicensePlate());
        car.setBrand(carRequestDTO.getBrandName());
        car.setModel(carRequestDTO.getModel());
        car.setColor(carRequestDTO.getColor());
        car.setNumberOfSeats(carRequestDTO.getNoOfSeats());
        car.setProductionYears(carRequestDTO.getProductionYear());
        car.setTransmissionType(carRequestDTO.getTransmission());
        car.setFuelType(carRequestDTO.getFuel());
        car.setMileage(carRequestDTO.getMileage());
        car.setFuelConsumption(carRequestDTO.getFuelConsumption());
        car.setBasePrice(carRequestDTO.getBasePrice());
        car.setDeposit(carRequestDTO.getDeposit());
        String city = carRequestDTO.getCityOrProvince();
        String district = carRequestDTO.getDistrict();
        String ward = carRequestDTO.getWard();
        String houseNumber = carRequestDTO.getHouseNumberOrStreet();
        String address = houseNumber.concat(" - ").
                concat(ward).concat(" - ").
                concat(district).concat(" - ").concat(city);
        car.setAddress(address);
        car.setDescription(carRequestDTO.getDescription());
        car.setAdditionalFunctions(carRequestDTO.getAdditionalFunctions());
        car.setTermsOfUse(carRequestDTO.getTermsOfUse());
        car.setStatus("Available");
        car.setRegistrationPaperUrl(carRequestDTO.getRegistrationPaperUrl());
        car.setCertificateOfInspectionUrl(carRequestDTO.getCertificateOfInspectionUrl());
        car.setInsuranceUrl(carRequestDTO.getInsuranceUrl());
        Carimage carimage = new Carimage();
        carimage.setFrontImageUrl(carRequestDTO.getFrontImageUrl());
        carimage.setBackImageUrl(carRequestDTO.getBackImageUrl());
        carimage.setLeftImageUrl(carRequestDTO.getLeftImageUrl());
        carimage.setRightImageUrl(carRequestDTO.getRightImageUrl());
        car.setCarImage(carimage);

        carimage.setCar(car);

        return car;
    }


    /**
     * Method to tranfer  from  Car to CarResponseDTO
     *
     * @param car
     * @return
     */
    public CarResponseDTO toSearchDTO(Car car,LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        CarResponseDTO carResponseDTO = new CarResponseDTO();
        carResponseDTO.setName(car.getName());
        carResponseDTO.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
        carResponseDTO.setBackImageUrl(car.getCarImage().getBackImageUrl());
        carResponseDTO.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
        carResponseDTO.setRightImageUrl(car.getCarImage().getRightImageUrl());
        Integer rating = findRatingOfCar(car.getId());
        carResponseDTO.setRating(rating);
        carResponseDTO.setNoOfRides(car.getDeposit());
        carResponseDTO.setPrice(car.getBasePrice());
        String location = car.getAddress().split(" - ")[2]
                .concat(" , ").concat(car.getAddress().split(" - ")[3]);
        carResponseDTO.setLocation(location);
        String status = addStatus(car.getId(),pickUpTime,dropOffTime);
        carResponseDTO.setStatus(status);
        return carResponseDTO;
    }

    /**
     * Cais nay rieng cho my car // roi vai of , thoi lam tam , toi uu sau
     * @param car
     * @return
     */
    public CarResponseDTO toDTO(Car car) {
        CarResponseDTO carResponseDTO = new CarResponseDTO();
        carResponseDTO.setName(car.getName());
        carResponseDTO.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
        carResponseDTO.setBackImageUrl(car.getCarImage().getBackImageUrl());
        carResponseDTO.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
        carResponseDTO.setRightImageUrl(car.getCarImage().getRightImageUrl());
        Integer rating = findRatingOfCar(car.getId());
        carResponseDTO.setRating(rating);
        carResponseDTO.setNoOfRides(car.getDeposit());
        carResponseDTO.setPrice(car.getBasePrice());
        carResponseDTO.setStatus(car.getStatus());
        return carResponseDTO;
    }

    /**
     * Method to
     * @param car
     * @return
     */
    public CarDetailResponseDTO toCarDetailsResponseDTO(Car car) {
        CarDetailResponseDTO carDetailResponseDTO = new CarDetailResponseDTO();
        carDetailResponseDTO.setLicensePlate(car.getLicensePlate());
        carDetailResponseDTO.setColor(car.getColor());
        carDetailResponseDTO.setBrandName(car.getBrand());
        carDetailResponseDTO.setModel(car.getModel());
        carDetailResponseDTO.setProductionYear(car.getProductionYears());
        carDetailResponseDTO.setNoOfSeats(car.getNumberOfSeats());
        carDetailResponseDTO.setTransmission(car.getTransmissionType());
        carDetailResponseDTO.setFuel(car.getFuelType());
        carDetailResponseDTO.setMileage(car.getMileage());
        carDetailResponseDTO.setFuelConsumption(car.getFuelConsumption());
        carDetailResponseDTO.setAddress(car.getAddress());
        carDetailResponseDTO.setDescription(car.getDescription());
        carDetailResponseDTO.setAdditionalFunctions(car.getAdditionalFunctions());
        carDetailResponseDTO.setBasePrice(car.getBasePrice());
        carDetailResponseDTO.setDeposit(car.getDeposit());
        carDetailResponseDTO.setTermsOfUse(car.getTermsOfUse());
        carDetailResponseDTO.setRegistrationPaperUrl(car.getRegistrationPaperUrl());
        carDetailResponseDTO.setCertificateOfInspectionUrl(car.getCertificateOfInspectionUrl());
        carDetailResponseDTO.setInsuranceUrl(car.getInsuranceUrl());
        carDetailResponseDTO.setFrontImageUrl(car.getCarImage().getFrontImageUrl());
        carDetailResponseDTO.setBackImageUrl(car.getCarImage().getBackImageUrl());
        carDetailResponseDTO.setLeftImageUrl(car.getCarImage().getLeftImageUrl());
        carDetailResponseDTO.setRightImageUrl(car.getCarImage().getRightImageUrl());
        return carDetailResponseDTO;
    }
    /**
     * Method ro saveFile
     *
     * @param file
     * @param rootPath
     * @param saveLocation
     * @return
     */
    public String saveFile(MultipartFile file, String rootPath, String saveLocation) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fullDir = rootPath + File.separator + saveLocation;
            File dir = new File(fullDir);
            if (!dir.exists()) dir.mkdirs(); // tạo thư mục nếu chưa tồn tại
            Path path = Paths.get(fullDir + File.separator + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return saveLocation + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }

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

}
