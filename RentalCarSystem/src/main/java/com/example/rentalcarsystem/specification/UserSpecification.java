package com.example.rentalcarsystem.specification;

import com.example.rentalcarsystem.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class UserSpecification {
    public static Specification<User> usernameLike(String fullName) {

        return (root, query, criteriaBuilder) -> (criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
                "%"+fullName.toLowerCase()+"%"));
    }

    public static Specification<User> emailLike(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),"%"+email+"%");
    }

    public static Specification<User> dateOfBirth(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), date);
    }

    public static Specification<User> roleLike(String roleName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("role.roleName")),"%"+roleName+"%");
    }

    public static Specification<User> addressLike(String address) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")),"%"+address+"%");
    }

    public static Specification<User> phoneNoLike(String phoneNo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNo")),"%"+phoneNo+"%");
    }
}


