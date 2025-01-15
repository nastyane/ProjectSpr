package com.vk.itmo.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_projects")
@IdClass(ConstraintTestProjectKey.class)
public class TestProject {

    @Id
    private UUID projectId;

    @Id
    private Long userId;

    @Id
    private LocalDateTime time;

    protected TestProject() {}

    public TestProject(UUID projectId, Long userId, LocalDateTime timestamp) {
        this.projectId = projectId;
        this.userId = userId;
        this.time = timestamp;
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

}