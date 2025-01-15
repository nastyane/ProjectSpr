package com.vk.itmo.dto;

import com.vk.itmo.dto.enums.MilestoneStatus;
import org.springframework.lang.NonNull;

public record MilestoneStatusDto(@NonNull MilestoneStatus status) {
}
