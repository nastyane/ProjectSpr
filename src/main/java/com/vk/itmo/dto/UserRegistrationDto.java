package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record UserRegistrationDto(
        @NonNull String username,
        @NonNull String password,
        @NonNull String email
) {}
