package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(max = 255)
    @Column(name = "FrontImageUrl")
    private String frontImageUrl;

    @Size(max = 255)
    @Column(name = "BackImageUrl")
    private String backImageUrl;

    @Size(max = 255)
    @Column(name = "LeftImageUrl")
    private String leftImageUrl;

    @Size(max = 255)
    @Column(name = "RightImageUrl")
    private String rightImageUrl;

    @OneToOne(mappedBy = "carImage", cascade = CascadeType.ALL)
    private Car car;
}