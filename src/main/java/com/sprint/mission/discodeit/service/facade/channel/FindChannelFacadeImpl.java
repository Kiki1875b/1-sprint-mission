package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindChannelFacadeImpl implements FindChannelFacade {

  private final ChannelService channelService;
  private final MessageService messageService;
  private final UserService userService;
  private final ReadStatusService readStatusService;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelResponseDto findChannelById(String channelId) {
    Channel channel = channelService.findChannelById(channelId);

    // TODO : UpdateChannelFacadeImpl 에 중복 ... 어떻게 할지 고민
    Instant lastMessageTime = Optional.ofNullable(messageService.getLatestMessageByChannel(channelId))
        .map(Message::getCreatedAt)
        .orElse(Instant.EPOCH);

    List<ReadStatus> participants = readStatusService.findAllByChannelId(channelId);

    List<User> users = participants.stream()
        .map(ReadStatus::getUser).toList();

    return channelMapper.toDto(channel, lastMessageTime, users);
  }

  @Override // TODO : 최적화 1순위
  @Transactional
  public List<ChannelResponseDto> findAllChannelsByUserId(String userId) {

    // userid 와 관련된 모든 readstatus
    List<ReadStatus> readStatuses = readStatusService.findAllReadStatusRelatedToUserId(userId);

    List<UUID> readStatusChannelIdsOfUser = readStatuses.stream().map(status -> status.getChannel().getId()).toList();


    List<Channel> channels = channelService.findAllPublicOrChannelsIn(readStatusChannelIdsOfUser);


    Map<UUID, Instant> latestMessagesByChannel = messageService.getLatestMessageForChannels(channels);

    Map<UUID, List<User>> participatingUsersOfChannel = readStatuses.stream()
        .collect(Collectors.groupingBy(
            rs -> rs.getChannel().getId(),
            Collectors.mapping(rs -> rs.getUser(), Collectors.toList())
        ));


    return channels.stream()
        .map(channel -> channelMapper.toDto(
            channel,
            latestMessagesByChannel.getOrDefault(channel.getId(), Instant.EPOCH),
            participatingUsersOfChannel.getOrDefault(channel.getId(), Collections.emptyList())
        )).toList();

  }
}
