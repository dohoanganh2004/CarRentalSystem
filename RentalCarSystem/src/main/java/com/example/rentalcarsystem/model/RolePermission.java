package com.example.rentalcarsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_permission")
public class RolePermission {
    @EmbeddedId
    private RolePermissionId id;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PermissionId", nullable = false)
    private Permission permission;

    @MapsId("roleID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

}