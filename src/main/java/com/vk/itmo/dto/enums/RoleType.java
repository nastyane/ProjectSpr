package com.vk.itmo.dto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleType {
    @JsonProperty("team leader")
    TEAM_LEADER,
    @JsonProperty("developer")
    DEVELOPER,
    @JsonProperty("tester")
    TESTER,
    @JsonProperty ("analyst")
    ANALYST
}
