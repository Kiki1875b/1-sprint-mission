package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelResponseDto;

public interface UpdateChannelFacade {
  ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);


}
