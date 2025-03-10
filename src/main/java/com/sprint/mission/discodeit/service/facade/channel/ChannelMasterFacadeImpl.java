package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelManagementService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ChannelMasterFacadeImpl implements ChannelMasterFacade {

  private final ChannelManagementService channelManagementService;

  private final MessageService messageService;
  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    Channel channel = channelManagementService.createPrivateChannel(
        channelMapper.toEntity(channelDto),
        channelDto.participantIds()
    );

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser).toList();

    return channelMapper.toDto(channel, Instant.EPOCH, participants);
  }

  @Override
  @Transactional
  public ChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    Channel channel = channelManagementService.createPublicChannel(channelMapper.toEntity(channelDto));
    return channelMapper.toDto(channel, Instant.EPOCH, Collections.emptyList());
  }

  @Override
  @Transactional
  public ChannelResponseDto getChannelById(String channelId) {
    Channel channel = channelService.findChannelById(channelId);

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser)
        .toList();

    Instant lastMessageTime = channelManagementService.getLastMessageTimeForChannel(channelId);

    return channelMapper.toDto(channel, lastMessageTime, participants);
  }

  @Override
  @Transactional
  public List<ChannelResponseDto> findAllChannelsByUserId(String userId) {

    List<Channel> channels = channelService.findAllChannelsByUserId(userId);
    Map<UUID, Instant> latestMessageTimeByChannel = messageService.getLatestMessageForChannels(channels);
    Map<UUID, List<User>> channelParticipants = channelMapper.channelToParticipants(channels);

    return channelMapper.toListResponse(channels, latestMessageTimeByChannel, channelParticipants);
  }

  @Override
  @Transactional
  public ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);

    Instant lastMessageTime = channelManagementService
        .getLastMessageTimeForChannel(channelId);


    List<User> users = channel.getStatuses().stream()
        .map(status -> status.getUser()).collect(Collectors.toList());
//    List<User> users = channelManagementService
//        .getChannelParticipants(channelId);

    return channelMapper.toDto(channel, lastMessageTime, users);
  }

  @Override
  @Transactional
  public void deleteChannel(String channelId) {
    channelService.deleteChannel(channelId);
  }

}
