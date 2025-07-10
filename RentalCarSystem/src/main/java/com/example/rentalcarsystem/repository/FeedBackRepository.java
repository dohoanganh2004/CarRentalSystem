package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByBookingId(Integer bookingId);
}
