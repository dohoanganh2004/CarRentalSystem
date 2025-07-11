package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingNo")
    private Booking booking;

    @Column(name = "Ratings")
    private Double ratings;

    @Lob
    @Column(name = "Content")
    private String content;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "DateTime")
    private Instant dateTime;

}