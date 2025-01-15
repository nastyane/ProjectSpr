package com.vk.itmo.data.entities;

import com.vk.itmo.dto.enums.BugreportStatus;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Entity
@Table(name = "bug_reports")
public class BugReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private UUID projectId;

    @NonNull
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private BugreportStatus status;

    protected BugReportEntity() {
    }

    public BugReportEntity(UUID projectId, String description, BugreportStatus status) {
        this.projectId = projectId;
        this.description = description;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public BugreportStatus getStatus() {
        return status;
    }

    public void setStatus(BugreportStatus status) {
        this.status = status;
    }
}
