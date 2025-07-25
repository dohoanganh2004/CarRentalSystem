package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.dto.request.car.SearchRequestDTO;
import com.example.rentalcarsystem.dto.response.car.CarDetailResponseDTO;
import com.example.rentalcarsystem.dto.response.car.CarResponseDTO;
import com.example.rentalcarsystem.dto.response.other.ListResultResponseDTO;
import com.example.rentalcarsystem.mapper.CarMapper;
import com.example.rentalcarsystem.model.*;
import com.example.rentalcarsystem.repository.*;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.specification.CarSpecification;
import com.example.rentalcarsystem.untils.FileStoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final CarImageRepository carImageRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CarMapper carMapper;
    private final FileStoreService fileStoreService;


    public CarServiceImpl(CarRepository carRepository, CarOwnerRepository carOwnerRepository,
                          CarImageRepository carImageRepository,
                          JwtTokenProvider jwtTokenProvider,
                          CarMapper carMapper, FileStoreService fileStoreService) {
        this.carRepository = carRepository;
        this.carOwnerRepository = carOwnerRepository;

        this.carImageRepository = carImageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.carMapper = carMapper;
        this.fileStoreService = fileStoreService;
    }

    /**
     * Method to create new Car
     *
     * @param carRequestDTO
     * @return base information of car after create
     */
    @Override
    public CarResponseDTO createNewCar(CarRequestDTO carRequestDTO, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        if (carRepository.existsByLicensePlate(carRequestDTO.getLicensePlate())) {
            throw new RuntimeException("Car already exists");
        }

        Carowner carowner = carOwnerRepository.findById(carOwnerId).orElseThrow(() -> new RuntimeException("Car owner not found"));

        Car newCar = carMapper.fromDTO(carRequestDTO, carowner);
        if (carRequestDTO.getFrontImage() != null && !carRequestDTO.getFrontImage().isEmpty()) {
            validateFile(carRequestDTO.getFrontImage());
            String frontImageUrl = fileStoreService.saveFile(carRequestDTO.getFrontImage(), "uploads", "frontImage");
            carRequestDTO.setFrontImageUrl(frontImageUrl);
        }

        if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
            validateFile(carRequestDTO.getBackImage());
            String backImageUrl = fileStoreService.saveFile(carRequestDTO.getBackImage(), "uploads", "backImage");
            carRequestDTO.setBackImageUrl(backImageUrl);
        }

        if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
            validateFile(carRequestDTO.getRightImage());
            String rightImageUrl = fileStoreService.saveFile(carRequestDTO.getRightImage(), "uploads", "rightImage");
            carRequestDTO.setRightImageUrl(rightImageUrl);
        }

        if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
            validateFile(carRequestDTO.getLeftImage());
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
     * Method to get car by car id
     *
     * @param id
     * @return detail of the car
     */
    @Override
    public CarDetailResponseDTO getCarById(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car Not Found"));
        CarDetailResponseDTO carDetailResponseDTO = carMapper.toDetailDTO(car);
        return carDetailResponseDTO;
    }




    /**
     * Method to get list car of owner
     *
     * @param request
     * @return list of owner car
     */
    @Override
    public Page<CarResponseDTO> getOwnerCar(HttpServletRequest request,
                                            String name,
                                            BigDecimal price,
                                            String status,
                                            Integer page,Integer size) {
        String token = getTokenFromRequest(request);
        Integer carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        Specification<Car> specification = (root, query, cb) -> cb.conjunction();
        if(name != null && !name.isEmpty()){
            specification = specification.and(CarSpecification.carNameLike(name));
        }
        if(price != null) {
            specification = specification.and(CarSpecification.priceLessThan(price));
        }
        if(status != null && !status.isEmpty()) {
            specification = specification.and(CarSpecification.statusLike(status));
        }
        specification = specification.and(CarSpecification.hasCarOwnerId(carOwnerId));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Car> listCarOfOwner = carRepository.findAll(specification, pageable);

        return listCarOfOwner.map(carMapper::toDTO);
    }

    /**
     * Method to find the available car
     * @param searchRequestDTO
     * @return list of available car
     */
    @Override
    public ListResultResponseDTO<CarResponseDTO> searchCar(SearchRequestDTO searchRequestDTO) {
        Instant startDateTime = searchRequestDTO.getPickupDateTime();
        Instant endDateTime = searchRequestDTO.getDropOffDateTime();
        String location = searchRequestDTO.getPickUpLocation();

        if(startDateTime.isBefore(Instant.now()) || endDateTime.isBefore(Instant.now())) {
            throw  new RuntimeException("Start date and end date are not in the past");
        }
        if(endDateTime.isBefore(startDateTime)) {
            throw new RuntimeException("Drop-off date time must be later and Pick-up date time, please try again.");
        }
        List<Car> listAvailableCar = carRepository.searchAvailableCars(location, startDateTime, endDateTime);
        if (listAvailableCar.isEmpty()) {
            return new ListResultResponseDTO("No cars match your credentials, please try \n" +
                    "again", Collections.emptyList());
        }
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();
        for (Car car : listAvailableCar) {
            CarResponseDTO carResponseDTO = carMapper.toDTO(car);

            carResponseDTOList.add(carResponseDTO);
        }
        return new ListResultResponseDTO<>("There are " + listAvailableCar.size() + " cars that are available for you!", carResponseDTOList);
    }

    /**
     * Method allows car owner up date their car
     *
     * @param carRequestDTO
     * @param carId id of the car want to update
     * @param request request from client
     * @return detail of the car after update
     */
    @Transactional
    public CarDetailResponseDTO updateCarDetails(CarRequestDTO carRequestDTO, Integer carId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);


        Carowner currentCarOwner = carOwnerRepository.findById(carOwnerId).orElseThrow(() -> new RuntimeException("Car owner not found"));


        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car with id " + carId + " not found"));


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

        String fullAddress = String.join(" - ", carRequestDTO.getHouseNumberOrStreet(), carRequestDTO.getWard(), carRequestDTO.getDistrict(), carRequestDTO.getCityOrProvince());
        car.setAddress(fullAddress);
        car.setIsActive(carRequestDTO.getIsActive());


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
                validateFile(carRequestDTO.getFrontImage());
                carImage.setFrontImageUrl(fileStoreService.saveFile(carRequestDTO.getFrontImage(), "uploads", "image/frontImage"));
            }
            if (carRequestDTO.getBackImage() != null && !carRequestDTO.getBackImage().isEmpty()) {
                validateFile(carRequestDTO.getBackImage());
                carImage.setBackImageUrl(fileStoreService.saveFile(carRequestDTO.getBackImage(), "uploads", "image/backImage"));
            }
            if (carRequestDTO.getLeftImage() != null && !carRequestDTO.getLeftImage().isEmpty()) {
                validateFile(carRequestDTO.getLeftImage());
                carImage.setLeftImageUrl(fileStoreService.saveFile(carRequestDTO.getLeftImage(), "uploads", "image/leftImage"));
            }
            if (carRequestDTO.getRightImage() != null && !carRequestDTO.getRightImage().isEmpty()) {
                validateFile(carRequestDTO.getRightImage());
                carImage.setRightImageUrl(fileStoreService.saveFile(carRequestDTO.getRightImage(), "uploads", "image/rightImage"));
            }
            carImageRepository.save(carImage);
        }

        Car updatedCar = carRepository.save(car);
        log.debug("Updated car: " + updatedCar);
        return carMapper.toDetailDTO(updatedCar);
    }

    /**
     * Method allows car owner stop renting the car
     * @param carId id of the car
     * @param request
     * @return notification to user
     */
    @Override
    public String  stopRentalCar(int carId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (car.getCarOwner().getId() != carOwnerId) {
            throw new RuntimeException("You are not allowed to update this car");
        }

        if ("Booked".equals(carMapper.getDynamicCarStatus(car))) {
            throw new RuntimeException("Do not allow to stop a car that is currently booked");
        }

        car.setIsActive(false);
        carRepository.save(car);
        String message = " Car has been stopped.Please go to My Car to view details.";
        return message;
    }




    /**
     * Method to validate enter file from user
     * @param file
     *
     */
    public void validateFile(MultipartFile file ) {
        if(file.isEmpty() || file == null) {
            throw new RuntimeException("File is empty");
        }
        String contentType = file.getContentType();
        if(!(contentType.endsWith(".jpg") || contentType.endsWith(".jpeg") || contentType.endsWith(".png"))) {
            throw new RuntimeException("Invalid file type.Please choose a valid image type");
        }
        long maxSize = 5*1024*1024;
        if(file.getSize() > maxSize) {
            throw new RuntimeException("File is too large to save to disk.Please choose another file.");
        }
    }
    /**
     * Get Token from request
     *
     * @param request
     * @return token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
