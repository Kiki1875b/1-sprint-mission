package com.sprint.mission.discodeit.dto.user_status;

import java.time.Instant;

public record UserStatusResponseDto (
    String id,

    String userId,
    Instant lastActivityAt

){
}
