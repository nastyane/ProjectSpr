package com.vk.itmo.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private Long managerId;

    @NonNull
    private String projectName;

    @NonNull
    private String description;

    protected ProjectEntity() {}

    public ProjectEntity(Long managerId, String projectName, String description) {
        this.managerId = managerId;
        this.projectName = projectName;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getManagerId() {
        return managerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }
}
