package com.example.rentalcarsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RolePermissionId implements Serializable {
    private static final long serialVersionUID = 6847206037583217325L;
    @NotNull
    @Column(name = "PermissionId", nullable = false)
    private Integer permissionId;

    @NotNull
    @Column(name = "RoleID", nullable = false)
    private Integer roleID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RolePermissionId entity = (RolePermissionId) o;
        return Objects.equals(this.permissionId, entity.permissionId) &&
                Objects.equals(this.roleID, entity.roleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, roleID);
    }

}