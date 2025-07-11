package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "BookingNo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CarID")
    private Car car;

    @Column(name = "StartDateTime")
    private Instant startDateTime;

    @Column(name = "EndDateTime")
    private Instant endDateTime;

    @Lob
    @Column(name = "DriversInformation")
    private String driversInformation;

    @Size(max = 50)
    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Size(max = 50)
    @Column(name = "Status", length = 50)
    private String status;

}