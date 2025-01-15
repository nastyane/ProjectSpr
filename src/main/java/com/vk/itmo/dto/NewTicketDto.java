package com.vk.itmo.dto;

import org.springframework.lang.NonNull;

// TODO проверить что если ты пихнёшь null вернётся ожидаемая ошибка (чё-то там bad_request или bad_entity)
// TODO + дописать тесты на эти кейсы
public record NewTicketDto(@NonNull String title, @NonNull String description) {}
