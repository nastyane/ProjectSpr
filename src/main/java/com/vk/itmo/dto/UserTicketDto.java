package com.vk.itmo.dto;

import com.vk.itmo.dto.enums.TicketStatus;
import org.springframework.lang.NonNull;

public record UserTicketDto(
        @NonNull String ticketId,
        @NonNull String title,
        @NonNull TicketStatus status
) {}