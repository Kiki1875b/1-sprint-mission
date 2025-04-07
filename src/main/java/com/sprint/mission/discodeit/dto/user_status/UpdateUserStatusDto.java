package com.sprint.mission.discodeit.dto.user_status;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;


public record UpdateUserStatusDto(
    @NotNull(message = "last activity time cannot be null")
    Instant newLastActivityAt
) {

}
