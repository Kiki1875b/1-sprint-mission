package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

import static com.sprint.mission.discodeit.constant.ChannelConstant.CHANNEL_NAME_MAX_LENGTH;
import static com.sprint.mission.discodeit.constant.ChannelConstant.CHANNEL_NAME_MIN_LENGTH;

public record CreateChannelDto(
    @Size(
        min = CHANNEL_NAME_MIN_LENGTH,
        max = CHANNEL_NAME_MAX_LENGTH
    )
    String name,
    String description
) {
}
