package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

public record CreateMessageDto(
    @NotBlank
    String content,
    @NotBlank
    String channelId,
    @NotBlank
    String authorId
) {
}
