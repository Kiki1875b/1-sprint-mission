package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
    String id, // messageId
    Instant createdAt,
    Instant updatedAt,
    String content,
    String channelId,
    UserResponseDto author,
    List<BinaryContentDto> attachments
) {
}
