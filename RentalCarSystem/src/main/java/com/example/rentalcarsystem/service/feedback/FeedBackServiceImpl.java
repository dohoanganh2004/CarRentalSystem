package com.example.rentalcarsystem.service.feedback;

import com.example.rentalcarsystem.dto.request.feedback.FeedBackRequestDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackResponseDTO;
import com.example.rentalcarsystem.model.Booking;
import com.example.rentalcarsystem.model.Feedback;
import com.example.rentalcarsystem.repository.BookingRepository;
import com.example.rentalcarsystem.repository.FeedBackRepository;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final BookingRepository bookingRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FeedBackServiceImpl(FeedBackRepository feedBackRepository, BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider) {
        this.feedBackRepository = feedBackRepository;
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Add feed back from customer
     * @param feedBackRequestDTO
     * @return
     */
    @Override
    public FeedBackResponseDTO createFeedBack(FeedBackRequestDTO feedBackRequestDTO) {
        Booking booking = bookingRepository.findBookingById(feedBackRequestDTO.getBookId());
        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setRatings(feedBackRequestDTO.getRating());
        feedback.setContent(feedBackRequestDTO.getComment());
        bookingRepository.save(booking);

        return new FeedBackResponseDTO("Thank you for your feedback!");
    }

    @Override
    public List<FeedBackReportDTO> getFeedBackReportByCaOwnerId(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Integer carOwnerId = jwtTokenProvider.getUserIdFromToken(token);
        List<FeedBackReportDTO> feedBackReportList = feedBackRepository.findFeedbackByCarOwnerId(carOwnerId);
        return feedBackReportList;
    }



    /**
     * Get Token from request
     *
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}




