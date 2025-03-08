package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteChannelFacadeImpl implements DeleteChannelFacade{

  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;
  @Override
  @Transactional
  public void deleteChannel(String channelId) {
    channelService.deleteChannel(channelId);
  }
}
