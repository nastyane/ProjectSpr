package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record UserBugreportDto(
        @NonNull String bugReportId,
        @NonNull String description,
        @NonNull String status
) {}
