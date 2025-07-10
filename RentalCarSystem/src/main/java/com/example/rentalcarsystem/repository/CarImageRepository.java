package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Carimage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<Carimage, Integer> {
}
