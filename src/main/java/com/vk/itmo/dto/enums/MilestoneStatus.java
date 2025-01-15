package com.vk.itmo.dto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MilestoneStatus {
    @JsonProperty("open")
    OPEN,
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("closed")
    CLOSED;
}