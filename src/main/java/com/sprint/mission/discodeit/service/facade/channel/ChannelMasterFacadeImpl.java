package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMasterFacadeImpl implements ChannelMasterFacade {

  private final CreateChannelFacade createChannelFacade;
  private final FindChannelFacade findChannelFacade;
  private final UpdateChannelFacade updateChannelFacade;
  private final DeleteChannelFacade deleteChannelFacade;
  @Override
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {
    return createChannelFacade.createPrivateChannel(channelDto);
  }

  @Override
  public ChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    return createChannelFacade.createPublicChannel(channelDto);
  }

  @Override
  public ChannelResponseDto getChannelById(String channelId) {
    return findChannelFacade.findChannelById(channelId);
  }

  @Override
  public List<ChannelResponseDto> findAllChannelsByUserId(String userId) {
    return findChannelFacade.findAllChannelsByUserId(userId);
  }

  @Override
  public ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    return updateChannelFacade.updateChannel(channelId, channelUpdateDto);

  }

  @Override
  public void deleteChannel(String channelId) {
    deleteChannelFacade.deleteChannel(channelId);
  }
}
