package com.vk.itmo.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "developer_ticket_assignments")
@IdClass(ConstraintTicketDeveloperKey.class)
public class DeveloperTicketAssignment {

    @Id
    private UUID ticketId;

    @Id
    private Long userId;

    protected DeveloperTicketAssignment() {}
    public DeveloperTicketAssignment(UUID ticketId, Long userId) {
        this.ticketId = ticketId;
        this.userId = userId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}