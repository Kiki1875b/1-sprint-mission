package com.sprint.mission.discodeit.dto.user_status;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record CreateUserStatusDto(
    @NotBlank
    String userId,
    Instant lastOnlineAt
) {
}
