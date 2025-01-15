package com.vk.itmo.dto;

import com.vk.itmo.dto.enums.BugreportStatus;
import org.springframework.lang.NonNull;

public record BugreportStatusDto(@NonNull BugreportStatus status) {
}
