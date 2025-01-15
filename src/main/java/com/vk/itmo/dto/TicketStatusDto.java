package com.vk.itmo.dto;

import com.vk.itmo.dto.enums.TicketStatus;
import org.springframework.lang.NonNull;

public record TicketStatusDto(@NonNull TicketStatus status) {
}
