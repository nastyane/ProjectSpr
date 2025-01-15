package com.vk.itmo.dto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TicketStatus {
    @JsonProperty("new")
    NEW,
    @JsonProperty("accepted")
    ACCEPTED,
    @JsonProperty("in_progress")
    IN_PROGRESS,
    @JsonProperty("completed")
    COMPLETED;
}