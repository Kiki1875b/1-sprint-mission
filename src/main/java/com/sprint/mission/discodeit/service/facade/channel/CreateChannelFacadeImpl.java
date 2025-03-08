package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateChannelFacadeImpl implements CreateChannelFacade{

  private final UserService userService;
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {
    Channel channel = channelService.createPrivateChannel(channelMapper.toEntity(channelDto));
    List<User> users = userService.validateAndFindAllUsersIn(channelDto.participantIds());
    readStatusService.createMultipleReadStatus(users, channel);


    return channelMapper.toDto(channel, Instant.EPOCH, users);
  }

  @Override
  @Transactional
  public ChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    Channel channel = channelService.createPublicChannel(channelMapper.toEntity(channelDto));
    return channelMapper.toDto(channel, Instant.EPOCH, Collections.emptyList());
  }
}
