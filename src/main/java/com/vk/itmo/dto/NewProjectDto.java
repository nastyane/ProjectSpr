package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record NewProjectDto(@NonNull String projectName, @NonNull String description) {
}
