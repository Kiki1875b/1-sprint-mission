package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;

public interface CreateChannelFacade {

  ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto);
  ChannelResponseDto createPublicChannel(CreateChannelDto channelDto);
}
