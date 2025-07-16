package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "paymenthistory")
public class PaymentHistory {
    @Id
    @Column(name = "PaymentId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingNo")
    private Booking booking;

    @ColumnDefault("0.00")
    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "Title")
    private String title;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "PaymentDate")
    private Instant paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;

}