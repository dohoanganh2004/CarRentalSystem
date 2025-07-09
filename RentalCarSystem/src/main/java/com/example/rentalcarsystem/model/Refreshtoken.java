package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refreshtoken")
public class Refreshtoken {
    @Id
    @Column(name = "TokenID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;

    @Lob
    @Column(name = "Token")
    private String token;

    @Column(name = "ExpiryDate")
    private Instant expiryDate;

    @ColumnDefault("0")
    @Column(name = "IsRevoked")
    private Boolean isRevoked;

}