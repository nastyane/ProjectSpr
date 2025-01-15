package com.vk.itmo.data.entities;

import com.vk.itmo.dto.enums.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Entity
@Table(name = "employers")
@IdClass(ConstraintEmploymentKey.class)
public class EmployerEntity {

    @Id
    private UUID projectId;

    @Id
    private Long userId;

    @NonNull
    private RoleType role;

    protected EmployerEntity() {}

    public EmployerEntity(UUID projectId, Long userId, RoleType role) {
        this.projectId = projectId;
        this.userId = userId;
        this.role = role;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RoleType getRole() {
        return role;
    }

}
