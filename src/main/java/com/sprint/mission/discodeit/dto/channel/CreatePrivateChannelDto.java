package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreatePrivateChannelDto(
    @NotEmpty
    List<String> participantIds
){
}
