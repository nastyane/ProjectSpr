package com.vk.itmo.data.entities;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record ConstraintTicketDeveloperKey(
        @NonNull UUID ticketId,
        @NonNull Long userId
) {}
