package com.example.rentalcarsystem.service.car;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.model.Carimage;
import com.example.rentalcarsystem.model.Carowner;
import com.example.rentalcarsystem.repository.CarOwnerRepository;
import com.example.rentalcarsystem.repository.CarRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final UserRepository userRepository;

    public CarServiceImpl(CarRepository carRepository, CarOwnerRepository carOwnerRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.carOwnerRepository = carOwnerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method to create new Car
     * @param carRequestDTO
     * @return
     */
    @Override
    public Car creareNewCar(CarRequestDTO carRequestDTO) {
        String rootPath = "C:\\Users\\PC\\Desktop\\CarRentalSystem\\RentalCarSystem\\image";
        String frontImageUrl = saveFile(carRequestDTO.getFrontImage(),rootPath,"frontImage");
        String backImageUrl = saveFile(carRequestDTO.getBackImage(),rootPath,"backImage");
        String rightImageUrl = saveFile(carRequestDTO.getRightImage(),rootPath,"rightImage");
        String leftImageUrl = saveFile(carRequestDTO.getLeftImage(),rootPath,"leftImage");
        carRequestDTO.setFrontImageUrl(frontImageUrl);
        carRequestDTO.setBackImageUrl(backImageUrl);
        carRequestDTO.setRightImageUrl(rightImageUrl);
        carRequestDTO.setLeftImageUrl(leftImageUrl);
        Car savedCar = fromDTOtoCar(carRequestDTO);
        carRepository.save(savedCar);
        log.info("New car created");
        return savedCar;

    }

    @Override
    public List<Car> getAllCars() {
        List<Car> carList = carRepository.findAll();
        return carList;
    }

    @Override
    public Optional<Car> getCarById(int id) {
        Car car = carRepository.findById(id).orElseThrow(()->new RuntimeException("Car not found"));
        return Optional.of(car);
    }

    @Override
    public List<Car> getCarByOwnerId(int id) {
        List<Car> listCarOfOwner = carRepository.findAll();
        return listCarOfOwner;
    }

    /**
     * Change information from CarRequestDTO to Car
     * @param carRequestDTO
     * @return
     */

    public Car fromDTOtoCar(CarRequestDTO carRequestDTO) {
        Car car = new Car();
        Carowner carOwner = carOwnerRepository.findById(carRequestDTO.getCarOwnerId()).orElse(null);
        car.setCarOwner(carOwner);
        car.setName(carRequestDTO.getName());
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
     * Method ro saveFile
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










}
