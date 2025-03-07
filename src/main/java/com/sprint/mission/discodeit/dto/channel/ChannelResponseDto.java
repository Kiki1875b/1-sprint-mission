package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;

public record ChannelResponseDto(
    String id,
    Channel.ChannelType type,
    String name,
    String description,
    List<UserResponseDto> participants,
    Instant lastMessageAt
) {
}
