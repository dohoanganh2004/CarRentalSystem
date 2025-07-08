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
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "PermissionId", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "PermissionName", length = 100)
    private String permissionName;

    @Size(max = 500)
    @Column(name = "Link", length = 500)
    private String link;

}