package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChannelManagementServiceImpl implements ChannelManagementService {

  private final ChannelService channelService;
  private final UserService userService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;

  @Override
  @Transactional
  public Channel createPrivateChannel(Channel channel, List<String> userIds) {

    List<User> participants = userService.validateAndFindAllUsersIn(userIds);

    List<ReadStatus> statuses = participants.stream()
        .map(user -> new ReadStatus(channel, user))
        .toList();

    statuses.stream().forEach(channel::addReadStatus);

    return channelService.createPrivateChannel(channel);
  }

  @Override
  public Channel createPublicChannel(Channel channel) {
    return channelService.createPublicChannel(channel);
  }


  @Override
  public Instant getLastMessageTimeForChannel(String channelId) {
    return Optional.ofNullable(messageService.getLatestMessageByChannel(channelId))
        .map(m -> m.getCreatedAt())
        .orElse(Instant.EPOCH);
  }

  @Override
  public List<User> getChannelParticipants(String channelId) {
    List<ReadStatus> participants = readStatusService.findAllByChannelId(channelId);
    return participants.stream().map(ReadStatus::getUser).toList();
  }
}
