package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record UserAssignDto(
        @NonNull String userId
) {}
