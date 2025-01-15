package com.vk.itmo.data.entities;

import com.vk.itmo.dto.enums.TicketStatus;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.UUID;


@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private UUID milestoneId;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    protected TicketEntity() {
    }

    public TicketEntity(UUID milestoneId, String title, String description, TicketStatus status) {
        this.milestoneId = milestoneId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMilestoneId() {
        return milestoneId;
    }

    public String getTitle() {
        return title;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
