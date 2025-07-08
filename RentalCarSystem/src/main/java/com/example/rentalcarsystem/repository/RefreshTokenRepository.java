package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Refreshtoken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Refreshtoken, Integer> {
  Optional<Refreshtoken>  findByToken(String token);
  boolean existsByToken(String token);
}
