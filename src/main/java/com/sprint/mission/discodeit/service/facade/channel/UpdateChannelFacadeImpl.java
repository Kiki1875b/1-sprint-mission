package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateChannelFacadeImpl implements UpdateChannelFacade{
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;
  private final ChannelMapper channelMapper;
  @Override
  @Transactional
  public ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {

    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);

    Instant lastMessageTime = Optional.ofNullable(messageService.getLatestMessageByChannel(channelId))
        .map(Message::getCreatedAt)
        .orElse(Instant.EPOCH);

    List<ReadStatus> participants = readStatusService.findAllByChannelId(channelId);
    List<User> users = participants.stream().map(ReadStatus::getUser).toList();

    return channelMapper.toDto(channel, lastMessageTime, users);
  }
}
