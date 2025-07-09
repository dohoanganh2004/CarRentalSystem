package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Carowner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOwnerRepository extends JpaRepository<Carowner,Integer> {
}
