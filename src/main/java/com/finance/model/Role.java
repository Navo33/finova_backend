package com.finance.model;

import jakarta.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue                     // PostgreSQL will use gen_random_uuid()
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID roleId;

    @Column(nullable = false, unique = true, length = 50)
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String roleName;

    // -----------------------------------------------------------------
    // Constructors, getters & setters
    // -----------------------------------------------------------------
    public Role() {}

    public UUID getRoleId() { return roleId; }
    public void setRoleId(UUID roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    @Override
    public String toString() {
        return "Role{roleId=" + roleId + ", roleName='" + roleName + '\'' + '}';
    }
}