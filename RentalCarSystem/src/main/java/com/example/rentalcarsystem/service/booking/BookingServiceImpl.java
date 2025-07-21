package com.example.rentalcarsystem.service.booking;

import com.example.rentalcarsystem.dto.CarBookingBaseInfoDTO;
import com.example.rentalcarsystem.dto.CarBookingDetailsDTO;
import com.example.rentalcarsystem.dto.CarInformationDTO;
import com.example.rentalcarsystem.dto.UserInfoDTO;
import com.example.rentalcarsystem.dto.booking.BookingDetailsDTO;
import com.example.rentalcarsystem.dto.booking.BookingInformationDTO;
import com.example.rentalcarsystem.dto.booking.BookingResultDTO;
import com.example.rentalcarsystem.dto.response.rental.MyRentalResponseDTO;
import com.example.rentalcarsystem.email.Email;
import com.example.rentalcarsystem.email.EmailService;
import com.example.rentalcarsystem.mapper.BookingMapper;
import com.example.rentalcarsystem.mapper.CarMapper;
import com.example.rentalcarsystem.model.*;
import com.example.rentalcarsystem.repository.*;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.untils.DateUntil;
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
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final EmailService emailService;
    private final DateUntil dateUntil;

    public BookingServiceImpl(BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider, CarMapper carMapper, CarRepository carRepository, UserRepository userRepository, CustomerRepository customerRepository, BookingMapper bookingMapper, PaymentHistoryRepository paymentHistoryRepository, EmailService emailService, DateUntil dateUntil) {
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.carMapper = carMapper;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.bookingMapper = bookingMapper;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.emailService = emailService;
        this.dateUntil = dateUntil;
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
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);

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
        // Kiem tra xem lich dat xe da ton tai hay chua ( check cho an toan )
        if (bookingRepository.existsByCarIdAndStartDateTimeAndEndDateTime(bookingInformationDTO.getCarId(), startDateTime, endDateTime)) {
            throw new RuntimeException("Booking already exits!.Please choose another car!");
        }

        booking.setStartDateTime(startDateTime);
        booking.setEndDateTime(endDateTime);
        Car car = carRepository.findById(bookingInformationDTO.getCarId()).orElse(null);
        booking.setCar(car);

        Customer customer = customerRepository.findById(customerId).orElse(null);
        booking.setCustomer(customer);

        User customerRentCar = customer.getUser();
        User carOwner = userRepository.findById(booking.getCar().getCarOwner().getId()).orElse(null);
        String message = "";

        // Tinh toan so tien phai tra
        BigDecimal deposit = calculateDeposit(booking.getStartDateTime(), booking.getEndDateTime(), booking.getCar().getBasePrice());
        BigDecimal customerWallet = customer.getUser().getWallet();// lay ra so tien trong vi nguoi thue xe
        BigDecimal carOwnerWallet = booking.getCar().getCarOwner().getUser().getWallet();


        // Neu nguoi dung thanh toan bang vi dien tu
        if (bookingInformationDTO.getPaymentMethod().equals("My wallet")) {
            // Kiem tra xem tien trong vi co du thanh toan tien dat coc hay khong!

            if (customerWallet.subtract(deposit).compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Customer wallet does not have enough money!");
            } else {
                // Neu co du tien thi tru luon tien trong vi cua nguoi thue xe
                customerWallet = customerWallet.subtract(deposit);
                customerRentCar.setWallet(customerWallet);
                userRepository.save(customerRentCar);
                booking.setPaymentMethod("My wallet");
                booking.setStatus("Confirmed");
            }

        } else {
            booking.setStatus("Pending deposit");
        }

        booking.setPaymentMethod(bookingInformationDTO.getPaymentMethod());

        bookingRepository.saveAndFlush(booking);

        // them vao lich su giao dich
        String payerTitle = " Your account has been deducted " + deposit + " for booking ";
        // Neu nguoi dung la car owner thi tai khoan se cong them
        String receiverTitle = "Yout account has been added " + deposit + " for booking ";
        // Them vao lich su thanh toan
        paymentHistoryBooking(customerRentCar, carOwner, deposit, booking, payerTitle, receiverTitle);

        // Sau khi xe duoc muon thi status cua car se thay doi tu available sang booked
        car.setStatus("Booked");
        carRepository.save(car);

        message = String.format("You've successfully booked %s from %s to %s.\n\nYour booking number is: %d\n\nOur operator will contact you with further guidance about pickup.", car.getName(), booking.getStartDateTime(), booking.getEndDateTime(), booking.getId());
        //Send Email to car owner
        sendEmailToCarOwnerAfterBooking(car.getCarOwner().getUser().getEmail(), car.getName(), Instant.now());

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
        // After cancel the booking , deposit will return customer's wallet
        User customer = userRepository.findUserById(customerId);
        User carOwner = userRepository.findUserById(booking.getCar().getCarOwner().getId());
        BigDecimal customerWallet = customer.getWallet();
        BigDecimal carOwnerWallet = carOwner.getWallet();
        // tinh toan so tien dat coc
        BigDecimal deposit = calculateDeposit(booking.getStartDateTime(), booking.getEndDateTime(), booking.getCar().getBasePrice());
        // Sau khi huy don , tien se se tra ve vi cua customer
        customerWallet = customerWallet.add(deposit);
        carOwnerWallet = carOwnerWallet.subtract(deposit);
        customer.setWallet(customerWallet);
        carOwner.setWallet(carOwnerWallet);
        userRepository.save(carOwner);
        userRepository.save(customer);
        // luu thong tin vao lich sua giao dich
        // Neu nguoi dung la customer , tai khoan se  cong them
        String payerTitle = " Your account has been added " + deposit + " for booking " + bookingId;
        // Neu nguoi dung la car owner thi tai khoan se tru di
        String receiverTitle = "Yout account has been deducted " + deposit + " for booking " + bookingId;
        // Them vao lich su thanh toan
        paymentHistoryBooking(customer, carOwner, deposit, booking, payerTitle, receiverTitle);
        // gui email thong bao co car owner
        sendEmailToCarOwnerAfterCancel(booking.getCar().getCarOwner().getUser().getEmail(), booking.getCar().getName(), Instant.now());
        return bookingMapper.toCarBookingBaseInfoDTO(booking);
    }

    /**
     * Method to calculate deposit of booking
     *
     * @param startDateTime time to pick up the car
     * @param endDateTime   time to return the car
     * @param basePrice     price of car
     * @return deposit of booking
     */
    public BigDecimal calculateDeposit(Instant startDateTime, Instant endDateTime, BigDecimal basePrice) {
        LocalDateTime starDate = startDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = endDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
        long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(numberOfDays)); // tinh tien tong dua tren ngay thue xe
        BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
        return deposit;
    }

    /**
     * Method allows customer can confirm pickup the car
     *
     * @param bookingId id of booking
     * @param request
     * @return base information of car
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
        if (booking.getStatus().equals("Confirmed")) {
            booking.setStatus("In-Progress");

        }
        bookingRepository.saveAndFlush(booking);
        return bookingMapper.toCarBookingBaseInfoDTO(booking);
    }




    /**
     * Method allows customer return the car
     *
     * @param bookingId id of booking
     * @param request request from client
     * @return string to notification to user
     */
    @Override
    public BookingResultDTO returnTheCar(Integer bookingId, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int customerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        Car car = booking.getCar();
        User carOwner = userRepository.findUserById(car.getCarOwner().getId());
        User customer = userRepository.findUserById(customerId);
        if (customer == null) {
            throw new RuntimeException("User not found");
        }
        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer is not owner of this booking!");
        }


        LocalDateTime starDate = booking.getStartDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // ngay tra phai lay ngay thuc the khi tra
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.systemDefault());
        LocalDateTime endDate = zonedNow.toLocalDateTime();
        long numberOfDays = ChronoUnit.DAYS.between(starDate, endDate);
        BigDecimal totalPrice = car.getBasePrice().multiply(BigDecimal.valueOf(numberOfDays)); // tinh tien tong dua tren ngay thue xe
        BigDecimal deposit = totalPrice.multiply(new BigDecimal("1.015")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal returnMoney = deposit.subtract(totalPrice);
        BigDecimal customerWallet = customer.getWallet();
        BigDecimal carOwnerWallet = carOwner.getWallet();
        //if total price less than deposit
        String payerTitle = "";
        String receiverTitle = "";
        if (totalPrice.compareTo(deposit) < 0) {
            customerWallet = customerWallet.add(returnMoney);
            receiverTitle = "Your account has been added " + returnMoney + " for booking " + bookingId;
            carOwnerWallet = carOwnerWallet.subtract(returnMoney);
            payerTitle = "Your account has been deducted " + returnMoney + " for booking " + bookingId;
            paymentHistoryBooking(carOwner, customer, deposit, booking, payerTitle, receiverTitle);

        }
        // if total price more than deposit
        if (totalPrice.compareTo(deposit) > 0) {
            if (totalPrice.subtract(deposit).compareTo(customerWallet) > 0) {
                booking.setStatus("Pending Payment");
                throw new RuntimeException("Your wallet doesn’t have enough balance. Please top-up your \n" + "wallet and try again");
            } else {
                customerWallet = customerWallet.subtract(totalPrice.subtract(deposit));
                payerTitle = "Your account has been deducted " + returnMoney + " for booking " + bookingId;
                carOwnerWallet = carOwnerWallet.add(totalPrice.subtract(deposit));
                receiverTitle = "Your account has been added " + returnMoney + " for booking " + bookingId;
                paymentHistoryBooking(customer, carOwner, deposit, booking, payerTitle, receiverTitle);
            }
        }
        booking.setStatus("Completed");
        car.setStatus("Available");
        carRepository.save(car);
        bookingRepository.saveAndFlush(booking);
        // Send mail
        sendEmailToCarOwnerAfterReturnCar(booking.getCar().getCarOwner().getUser().getEmail(), booking.getCar().getName(), Instant.now());
        String message = "Thank you for returning the car!.See you soon!";
        return new BookingResultDTO(message);


    }

    /**
     * Method to confirm deposit
     *
     * @param request
     * @return
     */
    @Override
    public String confirmDeposit(HttpServletRequest request, Integer bookingId) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getCar().getCarOwner().getId() != carOwnerId) {
            throw new RuntimeException("You are not owner of this car");
        }
        ;
        booking.setStatus("Confirmed");
        bookingRepository.saveAndFlush(booking);
        String message = "Thank you for confirming the deposit";
        return message;
    }

    /**
     * Method allows to car owner confirm payment
     *
     * @param request
     * @param bookingId
     * @return
     */
    @Override
    public String confirmPayment(HttpServletRequest request, Integer bookingId) {
        String token = getTokenFromRequest(request);
        int carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getCar().getCarOwner().getId() != carOwnerId) {
            throw new RuntimeException("You are not owner of this car");
        }
        if (booking.getStatus().equals("Pending Payment")) {
            booking.setStatus("Confirmed");
        }
        String message = "Thank you for confirming the payment";
        return message;
    }


    /**
     * Method to send email to car owner after customer booking their car
     *
     * @param toEmail     email will receive
     * @param carName     name of booking car
     * @param bookingTime time booking car
     */
    public void sendEmailToCarOwnerAfterBooking(String toEmail, String carName, Instant bookingTime) {
        String subject = "Your car has been booked";
        String body = "Congratulations! Your car " + carName + " has been booked at \n" + bookingTime + ". Please go to your wallet to check if the deposit \n" + "has been paid and go to your car’s details page to confirm the deposit. \n" + "Thank you!";
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject(subject);
        email.setBody(body);
        emailService.sendEmail(email);
    }

    /**
     * Method to send email to car owner  after customer cancel booking
     *
     * @param toEmail    email will receive
     * @param carName    name of booking car
     * @param cancelTime time cancel the booking
     */
    public void sendEmailToCarOwnerAfterCancel(String toEmail, String carName, Instant cancelTime) {
        String subject = "A booking with your car has been cancelled";
        String body = ": Please be informed that a booking with your car" + carName + " \n" + "has been cancelled at" + cancelTime + ". The deposit will be \n" + "returned to the customer’s wallet";
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject(subject);
        email.setBody(body);
        emailService.sendEmail(email);
    }

    /**
     * Method to send email to car owner after customer return car
     *
     * @param toEmail    email will receive mail
     * @param carName    name of booking car
     * @param returnTime time return the car
     */
    public void sendEmailToCarOwnerAfterReturnCar(String toEmail, String carName, Instant returnTime) {
        String subject = "Your car has been returned";
        String body = "Please be informed that your car" + carName + " has been \n" + "returned at" + returnTime + ". Please go to your wallet to check if \n" + "the remaining payment has been paid and go to your car’s details page\n" + "to confirm the payment. Thank you!";
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject(subject);
        email.setBody(body);
        emailService.sendEmail(email);
    }

    /**
     * Method to save change of user wallet
     *
     * @param payer
     * @param receiver
     * @param amount
     * @param booking
     * @param payerTitle
     * @param receiverTitle
     */
    public void paymentHistoryBooking(User payer, User receiver, BigDecimal amount, Booking booking, String payerTitle, String receiverTitle) {
        if (payer.getWallet().compareTo(amount) < 0) {
            throw new RuntimeException("Please top up more money to your wallet");
        }
        // luu lich su bien dong so du cua nguoi chuyen khoan
        PaymentHistory payerPaymentHistory = new PaymentHistory();
        payerPaymentHistory.setAmount(amount);
        payerPaymentHistory.setTitle(payerTitle);
        payerPaymentHistory.setPaymentDate(Instant.now());
        payerPaymentHistory.setBooking(booking);
        payerPaymentHistory.setUser(payer);
        paymentHistoryRepository.save(payerPaymentHistory);
        // luu lich su bien dong so du cua tai khoan nguoi nhan tien
        PaymentHistory receiverPaymentHistory = new PaymentHistory();
        receiverPaymentHistory.setAmount(amount);
        receiverPaymentHistory.setTitle(receiverTitle);
        receiverPaymentHistory.setPaymentDate(Instant.now());
        receiverPaymentHistory.setBooking(booking);
        receiverPaymentHistory.setUser(receiver);
        paymentHistoryRepository.save(receiverPaymentHistory);

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
