package com.example.rentalcarsystem;


import com.example.rentalcarsystem.service.car.CarServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RentalCarSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RentalCarSystemApplication.class, args);
        CarServiceImpl carService = context.getBean(CarServiceImpl.class);

        System.out.println(carService.findRatingOfCar(1));


    }

}
