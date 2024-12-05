package com.finance.model;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID roleId;

    @Column(nullable = false, unique = true)
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String roleName;


    // Default constructor
    public Role() {}

    // Getters and Setters
    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
