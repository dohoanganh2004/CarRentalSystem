package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.Blacklisttoken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<Blacklisttoken,Integer> {
    boolean existsByToken(String token);
}
