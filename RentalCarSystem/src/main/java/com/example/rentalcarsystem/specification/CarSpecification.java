package com.example.rentalcarsystem.specification;

import com.example.rentalcarsystem.model.Car;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class CarSpecification {
    public static Specification<Car> carNameLike(String carName) {
       return (root, criteriaQuery, criteriaBuilder) ->
               criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+carName.toLowerCase()+"%");
    }

    public static Specification<Car> statusLike(String status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),"%"+status+"%");
    }

    public static Specification<Car> priceLessThan(BigDecimal price) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Car> hasCarOwnerId(Integer carOwnerId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("carOwner").get("id"), carOwnerId);
    }
}
