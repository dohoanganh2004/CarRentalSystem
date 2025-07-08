package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.dto.request.user.UserRequestDTO;
import com.example.rentalcarsystem.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = "role")
    Optional<User> getUserByEmail(String email);

    User findUserByEmail(@Size(max = 100) @NotNull String email);

    User findUserById(Integer id);

    boolean existsByEmailAndPhoneNo(@Size(max = 100) @NotNull String email, @Size(max = 20) String phoneNo);

    boolean existsByPhoneNoAndIdNot(@Size(max = 20) String phoneNo, Integer id);
    boolean existsByEmailAndIdNot(@Size(max = 100) @NotNull String email, @Size(max = 20) Integer id);
    @EntityGraph(attributePaths = "role")
    Optional<User> findById(Integer id);
}
