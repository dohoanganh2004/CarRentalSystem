package com.example.rentalcarsystem.controller;

import com.example.rentalcarsystem.dto.request.car.CarRequestDTO;
import com.example.rentalcarsystem.model.Car;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import com.example.rentalcarsystem.service.car.CarServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Show all car
     * @return
     */
    @GetMapping
   public List<Car> getAllCars() {
       return carService.getAllCars();
   }

    /**
     * Get car by Id to view detail
     * @param id
     * @return
     */
   @GetMapping("/{id}")
   public Optional<Car> getCarById(@PathVariable int id) {
       return  carService.getCarById(id);
   }

    /**
     * Add car for rental
     * @param carRequestDTO
     * @return
     */
    @PostMapping("/add-car")
    public ResponseEntity<Car> creareNewCar (CarRequestDTO carRequestDTO) {
     return ResponseEntity.ok(carService.creareNewCar(carRequestDTO));
    }

    /**
     * Owner can view their car
     * @param request
     * @return
     */
    @GetMapping("/myCar")
    public List<Car> getCarByOwnerId( HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        return carService.getCarByOwnerId(userId);
    }

    /**
     * Get Token from request
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
