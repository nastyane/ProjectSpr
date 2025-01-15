package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

public record UserLoginDto(
        @NonNull String username,
        @NonNull String password
) {}
