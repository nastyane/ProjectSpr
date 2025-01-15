package com.vk.itmo.data.entities;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record ConstraintEmploymentKey(
        @NonNull UUID projectId,
        @NonNull Long userId
) {}
