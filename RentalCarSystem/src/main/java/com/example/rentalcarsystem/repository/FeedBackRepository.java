package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO;
import com.example.rentalcarsystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {
    @Query("""
    SELECT new com.example.rentalcarsystem.dto.response.feedback.FeedBackReportDTO(
        AVG(f.ratings),               
        u.fullName,                     
        f.content,                     
        f.dateTime,                     
        c.name,                         
        ci.frontImageUrl,            
        b.startDateTime,                
        b.endDateTime                   
    )
    FROM Feedback f
    JOIN f.booking b
    JOIN b.customer cus
    JOIN cus.user u
    JOIN b.car c
    LEFT JOIN c.carImage ci
    WHERE c.carOwner.id = :carOwnerId
    GROUP BY f.id, u.fullName, f.content, f.dateTime,
             c.name, ci.frontImageUrl, b.startDateTime, b.endDateTime
""")
    List<FeedBackReportDTO> findFeedbackByCarOwnerId(@Param("carOwnerId") int carOwnerId);



}
