package com.example.rentalcarsystem.service.feedback;

import com.example.rentalcarsystem.dto.request.feedback.FeedBackRequestDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackResponseDTO;
import com.example.rentalcarsystem.dto.response.feedback.PublicCarFeedBackDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FeedBackService {
FeedBackResponseDTO createFeedBack(FeedBackRequestDTO feedBackRequestDTO);
List<FeedBackReportDTO>  getFeedBackReportByCaOwnerId( HttpServletRequest request);
List<PublicCarFeedBackDTO> getFeedBackByCarId(Integer carId);
}
