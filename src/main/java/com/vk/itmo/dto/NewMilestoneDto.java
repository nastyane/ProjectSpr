package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDate;

public record NewMilestoneDto(
        @NonNull String projectName,
        @NonNull LocalDate startDate,
        @NonNull LocalDate endDate) {
}
