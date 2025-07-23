package com.example.rentalcarsystem.service.feedback;

import com.example.rentalcarsystem.dto.request.feedback.FeedBackRequestDTO;
import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;
import com.example.rentalcarsystem.dto.response.feedback.PublicCarFeedBackDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FeedBackService {
String createFeedBack(FeedBackRequestDTO feedBackRequestDTO);
FeedBackReportDTO  getFeedBackReportByCarId( HttpServletRequest request,Integer carId);
List<PublicCarFeedBackDTO> getFeedBackByCarId(Integer carId);
}
