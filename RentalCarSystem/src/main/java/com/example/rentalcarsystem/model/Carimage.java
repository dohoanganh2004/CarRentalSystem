package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "carimage")
public class Carimage {
    @Id
    @Column(name = "CarImageID", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "CarImageLink")
    private String carImageLink;

}