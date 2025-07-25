package com.example.rentalcarsystem.service.feedback;

import com.example.rentalcarsystem.dto.request.feedback.FeedBackRequestDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;
import com.example.rentalcarsystem.dto.response.feedback.PublicCarFeedBackDTO;
import com.example.rentalcarsystem.dto.response.feedback.UserFeedBackDTO;
import com.example.rentalcarsystem.model.Booking;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.model.Feedback;
import com.example.rentalcarsystem.repository.BookingRepository;
import com.example.rentalcarsystem.repository.CarRepository;
import com.example.rentalcarsystem.repository.FeedBackRepository;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final BookingRepository bookingRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CarRepository carRepository;

    public FeedBackServiceImpl(FeedBackRepository feedBackRepository, BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider, CarRepository carRepository) {
        this.feedBackRepository = feedBackRepository;
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.carRepository = carRepository;
    }

    /**
     * Method allows customer to feedback booking after return the car
     * @param feedBackRequestDTO
     * @return notification to people
     */
    @Override
    public String createFeedBack(FeedBackRequestDTO feedBackRequestDTO) {
        Booking booking = bookingRepository.findBookingById(feedBackRequestDTO.getBookId());
        if(!booking.getStatus().equals("Completed")) {
           throw new RuntimeException("Booking is not returned");
        }
        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setRatings(feedBackRequestDTO.getRating());
        feedback.setContent(feedBackRequestDTO.getComment());
        feedback.setDateTime(Instant.now());
        feedBackRepository.save(feedback);
        String message = "Thank you for your feedback!";
        return message;
    }



    /**
     * Method allows car owner can view feed back of this car
     * @param request
     * @return feedback report from user about car
     */
    @Override
    public FeedBackReportDTO getFeedBackReportByCarId(HttpServletRequest request,Integer carId) {
        String token = getTokenFromRequest(request);
        Integer carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        Car car = carRepository.findById(carId).orElseThrow(()->new RuntimeException("Car not found"));
        if(!car.getCarOwner().getId().equals(carOwnerId)) {
           throw new RuntimeException("You are not the owner of the car");
        }

        List<UserFeedBackDTO> feedBackReportList = feedBackRepository.findFeedbackByCarId(carId);
        Double ratingOfCar = findRatingOfCar(carId);
        FeedBackReportDTO feedBackReport = new FeedBackReportDTO();
        feedBackReport.setAverageRating(ratingOfCar);
        feedBackReport.setUserFeedBackDTO(feedBackReportList);
        return feedBackReport;

    }

    /**
     * Method get all feedback of a car by car id
     * @param carId id of the car
     * @return list of public feedback
     */
    @Override
    public List<PublicCarFeedBackDTO> getFeedBackByCarId(Integer carId) {
        List<PublicCarFeedBackDTO>  listPublicFeedBack =  feedBackRepository.findPublicFeedBackByCarId(carId);
        return listPublicFeedBack;
    }



    /**
     * Method to get token from request
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

    /**
     * Method to calculate rating of car
     * @param carId id of car what user want to view rating
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
        averageRating =   sumRating / totalFeedbackCount;
        rating = Math.ceil(averageRating*2)/2;
        return rating;
    }
}




