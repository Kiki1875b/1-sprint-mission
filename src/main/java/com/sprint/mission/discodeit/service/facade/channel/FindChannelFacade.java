package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;

import java.util.List;

public interface FindChannelFacade {
  ChannelResponseDto findChannelById(String channelId);
  List<ChannelResponseDto> findAllChannelsByUserId(String userId);
}
