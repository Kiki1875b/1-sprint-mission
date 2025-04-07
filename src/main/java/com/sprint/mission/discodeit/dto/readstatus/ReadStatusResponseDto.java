package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;

public record ReadStatusResponseDto(
    String id,
    String userId,
    String channelId,
    Instant lastReadAt
) {
}
