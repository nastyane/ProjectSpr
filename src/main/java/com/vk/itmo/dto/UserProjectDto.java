package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record UserProjectDto(
        @NonNull String projectId,
        @NonNull String projectName,
        @NonNull String role
) {}