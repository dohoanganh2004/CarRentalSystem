package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @Column(name = "FeedbackID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingNo")
    private Booking bookingNo;

    @Column(name = "Ratings")
    private Integer ratings;

    @Lob
    @Column(name = "Content")
    private String content;

    @Column(name = "DateTime")
    private Instant dateTime;

}