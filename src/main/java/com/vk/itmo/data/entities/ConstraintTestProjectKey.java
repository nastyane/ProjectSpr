package com.vk.itmo.data.entities;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConstraintTestProjectKey(
        @NonNull UUID projectId,
        @NonNull Long userId,
        @NonNull LocalDateTime time
) {}
