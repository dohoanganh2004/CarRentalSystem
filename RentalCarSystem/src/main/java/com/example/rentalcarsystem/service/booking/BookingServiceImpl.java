package com.example.rentalcarsystem.service.booking;

import com.example.rentalcarsystem.dto.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.CarInformationDTO;
import com.example.rentalcarsystem.dto.UserInfoDTO;
import com.example.rentalcarsystem.dto.booking.BookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.booking.BookingResultDTO;
import com.example.rentalcarsystem.dto.response.rental.MyRentalResponseDTO;
import com.example.rentalcarsystem.mapper.BookingMapper;
import com.example.rentalcarsystem.mapper.CarMapper;
import com.example.rentalcarsystem.model.Booking;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.model.Customer;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.BookingRepository;
import com.example.rentalcarsystem.repository.CarRepository;
import com.example.rentalcarsystem.repository.CustomerRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CarMapper carMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider, CarMapper carMapper, CarRepository carRepository, UserRepository userRepository, CustomerRepository customerRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.carMapper = carMapper;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Get All Car Booking by CustomerID
     *
     * @param request
     * @return
     */
    @Override
    public MyRentalResponseDTO listBookings(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Integer customerId = jwtTokenProvider.getUserIdFromToken(token);
        List<Booking> listBooking = bookingRepository.findBookingByCustomer_Id(customerId);
        List<CarBookingBaseInfoDTO> listCarBooking = new ArrayList<>();

        if (listBooking.isEmpty()) {
            return new MyRentalResponseDTO("No result!", listCarBooking);
        } else {
            for (Booking booking : listBooking) {
                CarBookingBaseInfoDTO carBookingBaseInfoDTO = new CarBookingBaseInfoDTO();
                carBookingBaseInfoDTO.setName(booking.getCar().getName());
                carBookingBaseInfoDTO.setFrom(booking.getStartDateTime());
                carBookingBaseInfoDTO.setTo(booking.getEndDateTime());
                LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endDate = booking.getEndDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
                long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
                carBookingBaseInfoDTO.setNumberOfDays(numberOfDays);
                BigDecimal basePrice = booking.getCar().getBasePrice();
                carBookingBaseInfoDTO.setBasePrice(basePrice);
                BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(numberOfDays));
                carBookingBaseInfoDTO.setTotal(totalPrice);
                BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
                carBookingBaseInfoDTO.setDeposit(deposit);
                carBookingBaseInfoDTO.setBookingStatus(booking.getStatus());
                carBookingBaseInfoDTO.setBookingNo(booking.getId());
                carBookingBaseInfoDTO.setFrontImageUrl(booking.getCar().getCarImage().getFrontImageUrl());
                carBookingBaseInfoDTO.setBackImageUrl(booking.getCar().getCarImage().getBackImageUrl());
                carBookingBaseInfoDTO.setLeftImageUrl(booking.getCar().getCarImage().getLeftImageUrl());
                carBookingBaseInfoDTO.setRightImageUrl(booking.getCar().getCarImage().getRightImageUrl());
                listCarBooking.add(carBookingBaseInfoDTO);
            }
        }

        return new MyRentalResponseDTO("There are " + listBooking.size() + " bookings in the system!", listCarBooking);
    }

    /**
     * Uodate detail boking
     *
     * @param bookingId
     * @param bookingDetailsDTO
     * @return
     */
    @Override
    public String updateBooking(Integer bookingId, BookingDetailsDTO bookingDetailsDTO) {
        Booking updatedBooking = bookingRepository.findBookingById(bookingId);
        if (updatedBooking == null) {
            throw new RuntimeException("Booking not found");
        }
        updatedBooking.setStartDateTime(bookingDetailsDTO.getPickupDateTime());
        updatedBooking.setEndDateTime(bookingDetailsDTO.getDropDateTime());
        bookingRepository.save(updatedBooking);
        String message = "Booking updated successfully";// ty them phan thong tin da update vao cho nguoi dung thay
        return message;
    }

    /**
     * Method to get booking detail by id
     *
     * @param bookingId id of booking
     * @return
     */
    @Override
    public CarBookingDetailsDTO getBookingById(Integer bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking with id " + bookingId + " not found");
        }
        CarBookingBaseInfoDTO carBookingBaseInfoDTO = new CarBookingBaseInfoDTO();
        CarBookingDetailsDTO carBookingDetailsDTO = new CarBookingDetailsDTO();


        // set car
        Car car = booking.getCar();
        CarInformationDTO carInformationDTO = carMapper.toInformationDTO(car);
        // set customer
        User customer = booking.getCustomer().getUser();
        System.out.println(customer.getFullName());
        UserInfoDTO customerInfo = new UserInfoDTO();
        customerInfo.setFullName(customer.getFullName());
        customerInfo.setPhone(customer.getPhoneNo());
        customerInfo.setNationalIdNo(customer.getNationalIDNo());
        customerInfo.setDateOfBirth(customer.getDateOfBirth());
        customerInfo.setEmail(customer.getEmail());
        customerInfo.setDrivingLicense(customer.getDrivingLicense());
        String cityOrProvince = customer.getAddress().split("-")[3];
        String district = customer.getAddress().split("-")[2];
        String ward = customer.getAddress().split("-")[1];
        String houseNumberOrStreet = customer.getAddress().split("-")[0];
        customerInfo.setHouseNumberOrStreet(houseNumberOrStreet);
        customerInfo.setWard(ward);
        customerInfo.setDistrict(district);
        customerInfo.setCityOrProvince(cityOrProvince);

        //get car owner
        User carOwner = booking.getCar().getCarOwner().getUser();
        System.out.println(carOwner.getFullName());
        UserInfoDTO carOwnerInfo = new UserInfoDTO();
        carOwnerInfo.setFullName(carOwner.getFullName());
        carOwnerInfo.setPhone(carOwner.getPhoneNo());
        carOwnerInfo.setNationalIdNo(carOwner.getNationalIDNo());
        carOwnerInfo.setDateOfBirth(carOwner.getDateOfBirth());
        carOwnerInfo.setEmail(carOwner.getEmail());
        carOwnerInfo.setDrivingLicense(carOwner.getDrivingLicense());
        String cityOrProvinceCarOwner = carOwner.getAddress().split("-")[3];
        String districtCarOwner = carOwner.getAddress().split("-")[2];
        String wardOwner = carOwner.getAddress().split("-")[1];
        String houseNumberOrStreetOwner = carOwner.getAddress().split("-")[0];
        carOwnerInfo.setHouseNumberOrStreet(houseNumberOrStreetOwner);
        carOwnerInfo.setWard(wardOwner);
        carOwnerInfo.setDistrict(districtCarOwner);
        carOwnerInfo.setCityOrProvince(cityOrProvinceCarOwner);


        // set care booking base

        carBookingBaseInfoDTO.setName(booking.getCar().getName());
        carBookingBaseInfoDTO.setFrom(booking.getStartDateTime());
        carBookingBaseInfoDTO.setTo(booking.getEndDateTime());
        LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = booking.getEndDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
        carBookingBaseInfoDTO.setNumberOfDays(numberOfDays);
        BigDecimal basePrice = booking.getCar().getBasePrice();
        carBookingBaseInfoDTO.setBasePrice(basePrice);
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(numberOfDays));
        carBookingBaseInfoDTO.setTotal(totalPrice);
        BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
        carBookingBaseInfoDTO.setDeposit(deposit);
        carBookingBaseInfoDTO.setBookingStatus(booking.getStatus());
        carBookingBaseInfoDTO.setBookingNo(booking.getId());
        carBookingBaseInfoDTO.setFrontImageUrl(booking.getCar().getCarImage().getFrontImageUrl());
        carBookingBaseInfoDTO.setBackImageUrl(booking.getCar().getCarImage().getBackImageUrl());
        carBookingBaseInfoDTO.setLeftImageUrl(booking.getCar().getCarImage().getLeftImageUrl());
        carBookingBaseInfoDTO.setRightImageUrl(booking.getCar().getCarImage().getRightImageUrl());

        // them thong tin
        carBookingDetailsDTO.setCarBookingBaseInfoDTO(carBookingBaseInfoDTO);
        carBookingDetailsDTO.setCarInformationDTO(carInformationDTO);
        carBookingDetailsDTO.setCustomerInfoDTO(customerInfo);
        carBookingDetailsDTO.setCarOwnerInfoDTO(carOwnerInfo);
        carBookingDetailsDTO.setPaymentMethod(booking.getPaymentMethod());
        carBookingDetailsDTO.setWallet(customer.getWallet());
        return carBookingDetailsDTO;
    }

    /**
     * Method allows customer to rent a car
     *
     * @param bookingInformationDTO
     * @return
     */
    @Override
    public BookingResultDTO rentalCar(BookingInformationDTO bookingInformationDTO, HttpServletRequest request) {
        Booking booking = new Booking();
        //Kiem tra thoi gian dat lich
        Instant startDateTime = bookingInformationDTO.getBookingDetailsDTO().getPickupDateTime();
        Instant endDateTime = bookingInformationDTO.getBookingDetailsDTO().getDropDateTime();
        // Kiem tra nguoi dung co  nhap thong tin ngay dat xe va tra xe ko
        if (startDateTime == null || endDateTime == null) {
            throw new RuntimeException("Start and end date must not be null");
        }

        // kiem tra thoi gian dat xe trong qua khu
        if (startDateTime.isBefore(Instant.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }

        // Kiem tra neu thoi gian tra xe som hon thoi gian dat xe
        if (!startDateTime.isBefore(endDateTime)) {
            throw new RuntimeException("Start date must be before end date");
        }

        booking.setStartDateTime(startDateTime);
        booking.setEndDateTime(endDateTime);
        Car car = carRepository.findById(bookingInformationDTO.getCarId()).orElse(null);
        booking.setCar(car);
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);
        Customer customer = customerRepository.findById(customerId).orElse(null);
        User user = userRepository.findById(customerId).orElse(null);
        booking.setCustomer(customer);
        booking.setStatus("PENDING");
        if (bookingInformationDTO.getPaymentMethod().equals("My wallet")) {
            BigDecimal customerWallet = customer.getUser().getWallet();// lay ra so tien trong vi nguoi thue xe
            LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endDate = booking.getEndDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
            long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
            BigDecimal totalPrice = car.getBasePrice().multiply(BigDecimal.valueOf(numberOfDays)); // tinh tien tong dua tren ngay thue xe
            BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
            ;// tinh tien dat coc
            // Kiem tra xem tien dat coc co du thanh toan khong
            if (customerWallet.subtract(deposit).compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Customer wallet does not have enough money!");
            } else {
                // Neu co du tien thi tru luon tien trong vi cua nguoi fung
                customerWallet = customerWallet.subtract(deposit);
                user.setWallet(customerWallet);
                userRepository.save(user);
            }
            booking.setPaymentMethod(bookingInformationDTO.getPaymentMethod());

        }
        // Kiem tra xem lich dat xe da ton tai hay chua ( check cho an toan )
        if (bookingRepository.existsByCarIdAndStartDateTimeAndEndDateTime(bookingInformationDTO.getCarId(), startDateTime, endDateTime)) {
            throw new RuntimeException("Booking already exits!.Please choose another car!");
        }
        //

        bookingRepository.saveAndFlush(booking);
        // Sau khi xe duoc muon thi status cua car se thay doi tu available sang booked
        car.setStatus("Booked");
        carRepository.save(car);

        String message = String.format("You've successfully booked %s from %s to %s%n%nYour booking number is: %d%n%nOur operator will contact you with further guidance about pickup.", car.getName(), booking.getStartDateTime(), booking.getEndDateTime(), booking.getId());
        return new BookingResultDTO(message);

    }

    /**
     * This method allow to customer can change status of booking
     *
     * @param bookingId id of Booking
     * @return base information of change status
     */
    @Override
    public CarBookingBaseInfoDTO cancelBooking(Integer bookingId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        String status = booking.getStatus();
        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer is not owner of this booking");
        }
        if (!(status.equals("Confirmed") || status.equals("Pending deposit") || status.equals("Stopped"))) {
            throw new RuntimeException("Booking status is not confirmed or Pending deposit or stopped");
        }
        booking.setStatus("Canceled");
        bookingRepository.saveAndFlush(booking);


        return bookingMapper.toCarBookingBaseInfoDTO(booking);
    }

    /**
     * Method allows customer can confirm pickup the car
     *
     * @param bookingId
     * @param request
     * @return
     */
    @Override
    public CarBookingBaseInfoDTO pickUpBooking(Integer bookingId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        String status = booking.getStatus();
        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer is not owner of this booking");
        }

        booking.setStatus("In-Progress");
        bookingRepository.saveAndFlush(booking);


        return bookingMapper.toCarBookingBaseInfoDTO(booking);

    }

    /**
     * Method allows customer return the car
     * @param bookingId
     * @param request
     * @return
     */
    @Override
    public BookingResultDTO returnTheCar(Integer bookingId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        Car car = booking.getCar();
      //  Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = userRepository.findUserById(customerId);
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer is not owner of this booking!");
        }
        booking.setStatus("Returned");
        car.setStatus("Available");
         carRepository.save(car);
        LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // ngay tra phai lay ngay thuc the khi tra
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.systemDefault());
        LocalDateTime endDate = zonedNow.toLocalDateTime();

        long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
        BigDecimal totalPrice = car.getBasePrice().multiply(BigDecimal.valueOf(numberOfDays)); // tinh tien tong dua tren ngay thue xe
        BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal returnMoney = deposit.subtract(totalPrice);
        BigDecimal customerWallet = user.getWallet();
        customerWallet = customerWallet.add(returnMoney);
        user.setWallet(customerWallet);
        userRepository.save(user);
        bookingRepository.saveAndFlush(booking);
        String message = "Thank you for returning the car!";
        return new BookingResultDTO(message);
    }


    /**
     * Get Token from request
     *
     * @param request
     * @return string token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
