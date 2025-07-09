package com.example.rentalcarsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "RoleID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "RoleName", length = 20)
    private String roleName;

}