package com.vk.itmo.dto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BugreportStatus {
    @JsonProperty("open")
    OPEN,
    @JsonProperty("fixed")
    FIXED,
    @JsonProperty("tested")
    TESTED,
    @JsonProperty("closed")
    CLOSED;
}
